package com.company.filehandlingapp;

import com.company.filehandlingapp.dao.ItemDao;
import com.company.filehandlingapp.model.file.StringFileOperation;
import com.company.filehandlingapp.model.item.Item;
import com.company.filehandlingapp.model.item.ItemOperation;
import com.company.filehandlingapp.model.item.ItemOperationConverter;
import com.company.filehandlingapp.model.item.ItemOperationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField operationAmountTextField;
    @FXML
    private ChoiceBox<String> operationTypeChoiceBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button importButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button addOperationButton;
    @FXML
    private Button deleteOperationButton;
    @FXML
    private TableView<ItemOperation> itemOperationTable = new TableView();
    @FXML
    private TableColumn<ItemOperation, ItemOperationType> tableColumnOperationType;
    @FXML
    private TableColumn<ItemOperation, Integer> tableColumnOperationAmount;
    private ObservableList<ItemOperation> itemOperations;
    private ItemDao itemDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static final String FXML_FILE_NAME_FOR_REPORT_VIEW = "report-view.fxml";
    private static final String DEFAULT_TITLE_MESSAGE = "Message";
    private static final String ADD_ITEM_OPERATION_ERROR_MESSAGE = "Type and amount attributes are required";
    private static final String SAVE_ITEM_ERROR_MESSAGE = "Name and at least one operation are required";
    private static final int MIN_REPORT_PANE_WIDTH = 275;
    private static final int MIN_REPORT_PANE_HEIGHT = 300;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.operationTypeChoiceBox.setItems(FXCollections.observableArrayList(
                "BUY", "SUPPLY"));
        addButtonEventHandlers();
        loadTable();
    }

    public void setItemDao(ItemDao dao) {
        this.itemDao = dao;
    }

    private void loadTable() {
        tableColumnOperationType.setCellValueFactory(new PropertyValueFactory<>("operationType"));
        tableColumnOperationAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        itemOperations = FXCollections.observableArrayList();
        itemOperationTable.setEditable(false);
        itemOperationTable.setItems(itemOperations);
        itemOperationTable.setRowFactory(param -> {
            final TableRow<ItemOperation> row = new TableRow();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int rowIndex = row.getIndex();
                if (rowIndex >= 0 && rowIndex < itemOperationTable.getItems().size() && itemOperationTable.getSelectionModel().isSelected(rowIndex)) {
                    itemOperationTable.getSelectionModel().clearSelection(rowIndex);
                    event.consume();
                }
            });
            return row;
        });
        itemOperationTable.setPlaceholder(new Label(""));
    }

    private void addButtonEventHandlers() {
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            saveItem();
            event.consume();
        });

        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    closeItemView();
                    event.consume();
                });

        addOperationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            addItemOperation();
            event.consume();
        });

        deleteOperationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            deleteItemOperation();
            event.consume();
        });

        importButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            importData();
            event.consume();
        });

        reportButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openReportView();
            event.consume();
        });
    }

    private Item createItem(String name, List<ItemOperation> operations) {
        Item item = new Item(name);
        operations.forEach(io -> {
            io.setItem(item);
            item.addOperation(io);
        });
        return item;
    }

    private void saveItem() {
        if (!itemNameTextField.getText().isEmpty() && itemOperations.size() > 0) {
            Item newItem = createItem(itemNameTextField.getText(), itemOperations);
            itemDao.save(newItem);
            setInitialStateOfItemViewComponents();
            closeItemView();
        } else {
            showAlert("Save operation message", DEFAULT_TITLE_MESSAGE, SAVE_ITEM_ERROR_MESSAGE);
        }
    }

    private void addItemOperation() {
        if (!operationAmountTextField.getText().isEmpty() && operationTypeChoiceBox.getValue() != null) {
            String choiceValue = operationTypeChoiceBox.getValue();
            String textValue = operationAmountTextField.getText();
            ItemOperation newItemOperation = new ItemOperation(ItemOperationType.valueOf(choiceValue), Integer.valueOf(textValue));
            itemOperations.add(newItemOperation);

            operationTypeChoiceBox.setValue("");
            operationAmountTextField.clear();
        } else {
            showAlert("Item operation message", DEFAULT_TITLE_MESSAGE, ADD_ITEM_OPERATION_ERROR_MESSAGE);
        }
    }

    private void deleteItemOperation() {
        int selectedTableRowIndex = itemOperationTable.getSelectionModel().getSelectedIndex();
        if (selectedTableRowIndex >= 0) {
            ItemOperation selectedItemOperation = itemOperationTable.getSelectionModel().getSelectedItem();
            itemOperations.remove(selectedItemOperation);
        }
    }

    public void importData() {
        setInitialStateOfItemViewComponents();
        String fileExt = "csv";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(fileExt.toUpperCase(Locale.ROOT), String.join("", "*", ".", fileExt))
        );
        File importedFile = fileChooser.showOpenDialog(pane.getScene().getWindow());
        if (Optional.ofNullable(importedFile).isPresent()) {
            String fileName = importedFile.getName().substring(0, importedFile.getName().indexOf(fileExt) - 1);
            StringFileOperation sfo = new StringFileOperation();
            List<String> importedData = sfo.read(importedFile.getPath());
            ItemOperationConverter ioc = new ItemOperationConverter();
            List<ItemOperation> importedItemOperations = ioc.convert(importedData, ",");
            itemNameTextField.setText(fileName);
            itemOperations.addAll(importedItemOperations);
            itemOperationTable.setItems(itemOperations);
        }
    }

    private void showAlert(String header, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }

    private void setInitialStateOfItemViewComponents() {
        itemNameTextField.clear();
        operationTypeChoiceBox.setValue("");
        operationAmountTextField.clear();
        itemOperations = FXCollections.observableArrayList();
    }

    private void closeItemView() {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

    private void openReportView() {
        if (!itemNameTextField.getText().isEmpty() && itemOperations.size() > 0) {
            Item item = createItem(itemNameTextField.getText(), itemOperations);
            URL reportViewResource = ItemController.class.getResource(ItemController.FXML_FILE_NAME_FOR_REPORT_VIEW);
            if (reportViewResource != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(reportViewResource);
                    Pane reportView = loader.load();
                    Stage reportStage = new Stage();
                    reportStage.initModality(Modality.APPLICATION_MODAL);
                    reportStage.setTitle("Report");
                    reportStage.setScene(new Scene(reportView, ItemController.MIN_REPORT_PANE_WIDTH, ItemController.MIN_REPORT_PANE_HEIGHT));
                    reportStage.setResizable(false);
                    ReportController reportController = loader.getController();
                    reportController.generateReport(item);
                    reportStage.showAndWait();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else {
                LOGGER.info("Resource is null");
            }
        } else {
            showAlert("Report operation message", DEFAULT_TITLE_MESSAGE, SAVE_ITEM_ERROR_MESSAGE);
        }
    }

}
