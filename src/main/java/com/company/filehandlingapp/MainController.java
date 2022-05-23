package com.company.filehandlingapp;

import com.company.filehandlingapp.dao.ItemDao;
import com.company.filehandlingapp.dao.ItemDaoImpl;
import com.company.filehandlingapp.environment.PersistenceSetup;
import com.company.filehandlingapp.model.item.Item;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private ItemDao itemDao;
    private static final String FXML_FILE_NAME_FOR_ITEM_VIEW = "item-view.fxml";
    private static final String FXML_FILE_NAME_FOR_REPORT_VIEW = "report-view.fxml";
    private static final String VIEW_TITLE_FOR_ADDING_ITEM_ATTRIBUTES = "Add attributes";
    private static final String VIEW_TITLE_FOR_IMPORTING_ITEM_ATTRIBUTES = "Import attributes";
    private static final String VIEW_TITLE_FOR_REPORT = "Report";
    private static final String WARNING_TITLE = "Warning";
    private static final String INFORMATION_TITLE = "Information";
    private static final String REMOVE_QUESTION_HEADER = "Delete selected row?";
    private static final String REMOVE_QUESTION_MESSAGE = "Data will be irreversibly removed";
    private static final String NO_SELECTION_HEADER = "No selected row";
    private static final String NO_SELECTION_MESSAGE = "Select row";
    private static final String OK_BUTTON_LABEL = "Yes";
    private static final String CANCEL_BUTTON_LABEL = "Cancel";
    private static final int MIN_ITEM_PANE_WIDTH = 350;
    private static final int MIN_ITEM_PANE_HEIGHT = 500;
    private static final int MIN_REPORT_PANE_WIDTH = 350;
    private static final int MIN_REPORT_PANE_HEIGHT = 300;

    private ButtonType okButton;
    private ButtonType cancelButton;

    @FXML
    private Parent mainPane;
    @FXML
    private TableView<Item> itemTable = new TableView();
    @FXML
    private TableColumn<Item, Item> tableColumnOrdinalNumber;
    @FXML
    private TableColumn<Item, String> tableColumnItemName;
    @FXML
    private Button addItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private Button generateReportButton;
    @FXML
    private MenuItem closeAppFromMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton = new ButtonType(OK_BUTTON_LABEL, ButtonBar.ButtonData.OK_DONE);
        cancelButton = new ButtonType(CANCEL_BUTTON_LABEL, ButtonBar.ButtonData.CANCEL_CLOSE);
        persistenceSetup();
        addButtonEventHandlers();
        addMenuItemHandlers();
        loadTable();
    }

    private void persistenceSetup() {
        PersistenceSetup pSetup = new PersistenceSetup("META-INF/hibernate.cfg.xml");
        SessionFactory sessionFactory = pSetup.getSessionFactory();
        itemDao = new ItemDaoImpl(sessionFactory);
    }

    private void addButtonEventHandlers() {
        addItemButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openItemView(VIEW_TITLE_FOR_ADDING_ITEM_ATTRIBUTES);
            event.consume();
        });

        deleteItemButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            removeItem();
            event.consume();
        });

        generateReportButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openReportView(VIEW_TITLE_FOR_REPORT);
            event.consume();
        });
    }

    private void addMenuItemHandlers() {
        closeAppFromMenuItem.setOnAction(event -> {
            Stage mainStage = (Stage) mainPane.getScene().getWindow();
            mainStage.close();
            event.consume();
        });
    }

    private void loadTable() {
        tableColumnOrdinalNumber.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue()));
        tableColumnOrdinalNumber.setCellFactory(new Callback<TableColumn<Item, Item>, TableCell<Item, Item>>() {
            @Override
            public TableCell<Item, Item> call(TableColumn<Item, Item> param) {
                return new TableCell<Item, Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);
                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        tableColumnOrdinalNumber.setSortable(false);
        tableColumnItemName.setCellValueFactory(new PropertyValueFactory<>("name"));

        itemTable.setRowFactory(param -> {
            final TableRow<Item> row = new TableRow();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int rowIndex = row.getIndex();
                if (rowIndex >= 0 && rowIndex < itemTable.getItems().size() && itemTable.getSelectionModel().isSelected(rowIndex)) {
                    itemTable.getSelectionModel().clearSelection(rowIndex);
                    event.consume();
                }
            });
            return row;
        });

        if (itemDao.findAll().size() > 0) {
            itemTable.setItems(FXCollections.observableArrayList(itemDao.findAll()));
        }
        itemTable.setPlaceholder(new Label(""));
    }

    private void updateTable() {
        List<Item> dbItems = itemDao.findAll();
        if (dbItems.size() > 0) {
            ObservableList<Item> items = FXCollections.observableArrayList(dbItems);
            itemTable.getItems().removeAll();
            itemTable.setItems(items);
        }
    }

    private void openItemView(String titleView) {
        URL itemViewResource = MainController.class.getResource(MainController.FXML_FILE_NAME_FOR_ITEM_VIEW);
        if (itemViewResource != null) {
            try {
                FXMLLoader loader = new FXMLLoader(itemViewResource);
                Pane itemView = loader.load();
                Stage itemStage = new Stage();
                itemStage.initModality(Modality.APPLICATION_MODAL);
                itemStage.setTitle(titleView);
                itemStage.setScene(new Scene(itemView, MainController.MIN_ITEM_PANE_WIDTH, MainController.MIN_ITEM_PANE_HEIGHT));
                itemStage.setResizable(false);
                ItemController itemController = loader.getController();
                itemController.setItemDao(itemDao);
                itemStage.showAndWait();
                updateTable();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.info("Resource is null");
        }
    }

    private void openReportView(String titleView) {
        int itemTableRowIndex = itemTable.getSelectionModel().getSelectedIndex();
        if (itemTableRowIndex >= 0) {
            Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
            URL reportViewResource = MainController.class.getResource(MainController.FXML_FILE_NAME_FOR_REPORT_VIEW);
            if (reportViewResource != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(reportViewResource);
                    Pane reportView = loader.load();
                    Stage reportStage = new Stage();
                    reportStage.initModality(Modality.APPLICATION_MODAL);
                    reportStage.setTitle(titleView);
                    reportStage.setScene(new Scene(reportView, MainController.MIN_REPORT_PANE_WIDTH, MainController.MIN_REPORT_PANE_HEIGHT));
                    reportStage.setResizable(false);
                    ReportController reportController = loader.getController();
                    reportController.generateReport(selectedItem);
                    reportStage.showAndWait();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else {
                LOGGER.info("Resource is null");
            }
        } else {
            showNoSelectionAlert();
        }
    }

    private void removeItem() {
        int selectedTableRowIndex = itemTable.getSelectionModel().getSelectedIndex();
        if (selectedTableRowIndex >= 0) {
            Alert confirmationAlert = new Alert(Alert.AlertType.WARNING, REMOVE_QUESTION_MESSAGE, okButton, cancelButton);
            confirmationAlert.setTitle(WARNING_TITLE);
            confirmationAlert.setHeaderText(REMOVE_QUESTION_HEADER);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response.getButtonData().equals(okButton.getButtonData())) {
                    Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
                    itemDao.delete(selectedItem);
                    updateTable();
                } else if (response.getButtonData().equals(cancelButton.getButtonData())) {
                    confirmationAlert.close();
                }
            });
        } else {
            showNoSelectionAlert();
        }
    }

    private void showNoSelectionAlert() {
        Alert noSelectionAlert = new Alert(Alert.AlertType.INFORMATION, NO_SELECTION_MESSAGE, ButtonType.OK);
        noSelectionAlert.setTitle(INFORMATION_TITLE);
        noSelectionAlert.setHeaderText(NO_SELECTION_HEADER);
        noSelectionAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                noSelectionAlert.close();
            }
        });
    }

}
