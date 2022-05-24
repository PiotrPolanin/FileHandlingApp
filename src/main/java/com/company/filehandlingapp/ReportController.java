package com.company.filehandlingapp;

import com.company.filehandlingapp.model.file.FileOperation;
import com.company.filehandlingapp.model.file.StringFileOperation;
import com.company.filehandlingapp.model.item.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    private Parent pane;
    @FXML
    private TextField buySumTextField;
    @FXML
    private TextField supplySumTextField;
    @FXML
    private TextField sumRemainderTextField;
    @FXML
    private Button exportButton;
    @FXML
    private Button cancelButton;
    private FileChooser fileChooser;
    private ItemOperationReportCalculator calculator;
    private OperationGenerator<ItemOperationReport> reportGenerator;
    private FileOperation sfo;
    private ItemOperationReport report;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Export data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
        calculator = new ItemOperationReportCalculator();
        reportGenerator = new ItemOperationReportGenerator();
        sfo = new StringFileOperation();
        addButtonEventHandlers();
    }

    private void addButtonEventHandlers() {
        buySumTextField.setEditable(false);
        supplySumTextField.setEditable(false);
        sumRemainderTextField.setEditable(false);

        exportButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            exportData(report);
            event.consume();
        });

        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    closeReportView();
                    event.consume();
                });
    }

    private void showReportDetails(ItemOperationReport report) {
        buySumTextField.setText(report.getBuySum().toString());
        supplySumTextField.setText(report.getSupplySum().toString());
        sumRemainderTextField.setText(report.getSumDiff().toString());
    }

    public void generateReport(Item item) {
        List<ItemOperation> itemOperations = new LinkedList<>(item.getOperations());
        Optional<ItemOperationReport> optionalReport = calculator.calculate(itemOperations);
        if (optionalReport.isPresent()) {
            ItemOperationReport newReport = optionalReport.get();
            showReportDetails(newReport);
            this.report = newReport;
        }
    }

    private void exportData(ItemOperationReport report) {
        File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        if (Optional.ofNullable(file).isPresent()) {
            List<String> exportedData = reportGenerator.generate(report);
            sfo.write(file.getPath(), exportedData);
            closeReportView();
        }
    }

    private void closeReportView() {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

}
