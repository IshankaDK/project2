package controller;

import bo.BoFactory;
import bo.custom.SeaFoodBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dto.SeaFoodDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import view.tm.SeaFoodTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class SeaFoodFormController {
    public AnchorPane root;
    public JFXTextField txtCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPurchasePrice;
    public JFXTextField txtSellPrice;
    public TextField txtSearch;
    public TableView<SeaFoodTM> tblSeaFood;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colPurchasePrice;
    public TableColumn colSellingPrice;
    public TableColumn colDeleteButton;
    public TableColumn colUpdateButton;
    public JFXButton btnAdd;

    SeaFoodBo bo;

    public void initialize() throws Exception {
        bo = BoFactory.getInstance().getBo(BoFactory.BOType.SEAFOOD);

        loadAllSeaFood();
        loadCode();

        colCode.setCellValueFactory(new PropertyValueFactory("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory("qtyOnHand"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory("sellingPrice"));
        colDeleteButton.setCellValueFactory(new PropertyValueFactory("btnDelete"));
        colUpdateButton.setCellValueFactory(new PropertyValueFactory("btnUpdate"));

        tblSeaFood.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    setData(newValue);
                });

    }

    private void setData(SeaFoodTM tm) {
        if(tm != null){
            txtCode.setText(tm.getCode());
            txtDescription.setText(tm.getDescription());
            txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
            txtPurchasePrice.setText(String.valueOf(tm.getPurchasePrice()));
            txtSellPrice.setText(String.valueOf(tm.getSellingPrice()));
        }

    }

    public void btnAddOnAction(ActionEvent actionEvent) {

        if(txtCode.getText().trim().length()>0 && txtDescription.getText().trim().length()>0 && txtQtyOnHand.getText().trim().length()>0 &&
                txtPurchasePrice.getText().trim().length()>0 && txtSellPrice.getText().trim().length()>0){
            try {
                boolean isAdded = bo.saveSeaFood(new SeaFoodDTO(txtCode.getText().trim(),txtDescription.getText().trim(),
                        Double.parseDouble(txtQtyOnHand.getText().trim()),Double.parseDouble(txtPurchasePrice.getText().trim()),
                        Double.parseDouble(txtSellPrice.getText().trim())));
                if (isAdded) {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Saved Successfully.!")
                            .text("You have Successfully save Item to the System.")
                            .graphic(new ImageView(new Image("/assert/done.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                    notificationBuilder.darkStyle();
                    notificationBuilder.show();
                    loadAllSeaFood();
                    clear();
                    loadCode();
                    txtCode.requestFocus();
                } else {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Saving UnSuccessful.!")
                            .text("Item Not Saved, Try Again.!")
                            .graphic(new ImageView(new Image("/assert/errorpng.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                    notificationBuilder.darkStyle();
                    notificationBuilder.show();
                    txtCode.requestFocus();
                }
            } catch (SQLException se){
                Notifications notificationBuilder = Notifications.create()
                        .title("Saving UnSuccessful.!")
                        .text("Item Not Saved, Try Again.!")
                        .graphic(new ImageView(new Image("/assert/errorpng.png")))
                        .hideAfter(Duration.seconds(4))
                        .position(Pos.BOTTOM_RIGHT);
                notificationBuilder.darkStyle();
                notificationBuilder.show();
                txtCode.requestFocus();
            } catch (Exception e) {
                Notifications notificationBuilder = Notifications.create()
                        .title("Saving UnSuccessful.!")
                        .text("Item Not Saved, Try Again.!")
                        .graphic(new ImageView(new Image("/assert/errorpng.png")))
                        .hideAfter(Duration.seconds(4))
                        .position(Pos.BOTTOM_RIGHT);
                notificationBuilder.darkStyle();
                notificationBuilder.show();
                txtCode.requestFocus();
            }
        }else {
            Notifications notificationBuilder = Notifications.create()
                    .title("Saving UnSuccessful.!")
                    .text("Item Not Saved, Some fields have been empty ..!")
                    .graphic(new ImageView(new Image("/assert/errorpng.png")))
                    .hideAfter(Duration.seconds(4))
                    .position(Pos.BOTTOM_RIGHT);
            notificationBuilder.darkStyle();
            notificationBuilder.show();
            txtCode.requestFocus();
        }
    }

    private void loadAllSeaFood() throws Exception {
        ArrayList<SeaFoodDTO> seaFoodDTOS = bo.getAllSeaFood();
        ObservableList<SeaFoodTM> tmList = FXCollections.observableArrayList();
        for (SeaFoodDTO dto : seaFoodDTOS) {
            Button btnDelete = new Button("Delete");
            btnDelete.setMaxSize(100, 50);
            btnDelete.setCursor(Cursor.HAND);
            btnDelete.setStyle("-fx-font-weight: bold ; -fx-background-color:  #D50000; ");
            btnDelete.setTextFill(Color.web("#FFFFFF"));
            Button btnUpdate = new Button("Update");
            btnUpdate.setMaxSize(100, 50);
            btnUpdate.setCursor(Cursor.HAND);
            btnUpdate.setStyle("-fx-font-weight: bold ; -fx-background-color:  #00BFA5;");
            SeaFoodTM tm = new SeaFoodTM(dto.getCode(),dto.getDescription(),dto.getQtyOnHand(),
                    dto.getPurchasePrice(),dto.getSellingPrice(),btnDelete,btnUpdate);
            tmList.add(tm);
            btnDelete.setOnAction((e) -> {
                try {
                    ButtonType ok = new ButtonType("OK",
                            ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO",
                            ButtonBar.ButtonData.CANCEL_CLOSE);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are You Want to Delete ?", ok, no);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.orElse(no) == ok) {
                        if (bo.deleteSeaFood(tm.getCode())) {
                            Notifications notificationBuilder = Notifications.create()
                                    .title("Delete Successful.!")
                                    .text("You have Successfully Delete Item from the System.")
                                    .graphic(new ImageView(new Image("/assert/done.png")))
                                    .hideAfter(Duration.seconds(4))
                                    .position(Pos.BOTTOM_RIGHT);
                            notificationBuilder.darkStyle();
                            notificationBuilder.show();
                            loadAllSeaFood();
                            clear();
                            loadCode();
                            return;
                        }
                        Notifications notificationBuilder = Notifications.create()
                                .title("Delete UnSuccessful.!")
                                .text("Item Not Deleted, Please try Again..!")
                                .graphic(new ImageView(new Image("/assert/errorpng.png")))
                                .hideAfter(Duration.seconds(4))
                                .position(Pos.BOTTOM_RIGHT);
                        notificationBuilder.darkStyle();
                        notificationBuilder.show();
                    }

                } catch (Exception e1) {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Delete UnSuccessful.!")
                            .text("Item Not Deleted, Please try Again..!")
                            .graphic(new ImageView(new Image("/assert/errorpng.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                    notificationBuilder.darkStyle();
                    notificationBuilder.show();
                }
            });
            btnUpdate.setOnAction((e) -> {
                try {
                    ButtonType ok = new ButtonType("OK",
                            ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("NO",
                            ButtonBar.ButtonData.CANCEL_CLOSE);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are You Want to Update ?", ok, no);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.orElse(no) == ok) {
                        if (bo.updateSeaFood(new SeaFoodDTO(txtCode.getText().trim(), txtDescription.getText().trim(),
                                Double.parseDouble(txtQtyOnHand.getText()), Double.parseDouble(txtPurchasePrice.getText()),
                                Double.parseDouble(txtSellPrice.getText())))) {
                            Notifications notificationBuilder = Notifications.create()
                                    .title("Update Successful.!")
                                    .text("You have Successfully Update Item from the System.")
                                    .graphic(new ImageView(new Image("/assert/done.png")))
                                    .hideAfter(Duration.seconds(4))
                                    .position(Pos.BOTTOM_RIGHT);
                            notificationBuilder.darkStyle();
                            notificationBuilder.show();
                            loadAllSeaFood();
                            clear();
                            loadCode();
                            return;
                        }
                        Notifications notificationBuilder = Notifications.create()
                                .title("Update UnSuccessful.!")
                                .text("Item Not Updated, Please try Again..!")
                                .graphic(new ImageView(new Image("/assert/errorpng.png")))
                                .hideAfter(Duration.seconds(4))
                                .position(Pos.BOTTOM_RIGHT);
                        notificationBuilder.darkStyle();
                        notificationBuilder.show();
                    }

                } catch (Exception e1) {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Update UnSuccessful.!")
                            .text("Item Not Updated, Please try Again..!")
                            .graphic(new ImageView(new Image("/assert/errorpng.png")))
                            .hideAfter(Duration.seconds(4))
                            .position(Pos.BOTTOM_RIGHT);
                    notificationBuilder.darkStyle();
                    notificationBuilder.show();
                }
            });
        }
        tblSeaFood.setItems(tmList);

        FilteredList<SeaFoodTM> filteredData = new FilteredList<>(tmList, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(seaFood -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (seaFood.getCode().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } else if (seaFood.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(seaFood.getQtyOnHand()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(seaFood.getPurchasePrice()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(seaFood.getSellingPrice()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<SeaFoodTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblSeaFood.comparatorProperty());
        tblSeaFood.setItems(sortedData);
    }

    public void clear() {
        txtCode.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtPurchasePrice.clear();
        txtSellPrice.clear();
        txtSearch.clear();

    }

    private void loadCode()  {
        String code = null;
        try {
            code = bo.getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtCode.setText(code);
    }

    public void txtSearchOnAction(ActionEvent actionEvent)  {
        SeaFoodDTO dto = null;
        try {
            dto = bo.getSeaFood(txtSearch.getText().trim());
        } catch (Exception e) {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error..!")
                    .text("Something Wrong.Please try Again..!")
                    .graphic(new ImageView(new Image("/assert/errorpng.png")))
                    .hideAfter(Duration.seconds(4))
                    .position(Pos.BOTTOM_RIGHT);
            notificationBuilder.darkStyle();
            notificationBuilder.show();
        }
        if(dto != null){
            txtCode.setText(dto.getCode());
            txtDescription.setText(dto.getDescription());
            txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));
            txtPurchasePrice.setText(String.valueOf(dto.getPurchasePrice()));
            txtSellPrice.setText(String.valueOf(dto.getSellingPrice()));
        }else{
            txtSearch.setStyle("-fx-border-color: #f53b57 ");
            txtSearch.requestFocus();
            Notifications notificationBuilder = Notifications.create()
                    .title("No Item Founded..!")
                    .text("Enter Valid Seafood Item Id.")
                    .graphic(new ImageView(new Image("/assert/errorpng.png")))
                    .hideAfter(Duration.seconds(4))
                    .position(Pos.BOTTOM_RIGHT);
            notificationBuilder.darkStyle();
            notificationBuilder.show();
        }
    }

    public void txtCodeOnAction(ActionEvent actionEvent) {
        if(Pattern.compile("^(SF)[0-9]{1,}$").matcher(txtCode.getText().trim()).matches()){
            txtCode.setFocusColor(Paint.valueOf("skyblue"));
            txtDescription.requestFocus();
        }else {
            txtCode.setFocusColor(Paint.valueOf("red"));
            txtCode.requestFocus();
        }
    }

    public void txtDescriptionOnAction(ActionEvent actionEvent) {
        if(Pattern.compile("^[A-z| |()]{1,}$").matcher(txtDescription.getText().trim()).matches()){
            txtDescription.setFocusColor(Paint.valueOf("skyblue"));
            txtQtyOnHand.requestFocus();
        }else {
            txtDescription.setFocusColor(Paint.valueOf("red"));
            txtDescription.requestFocus();
        }
    }

    public void txtQtyOnHandOnAction(ActionEvent actionEvent) {
        if (Pattern.compile("^[\\d|.]{1,4}$").matcher(txtQtyOnHand.getText().trim()).matches()) {
            txtQtyOnHand.setFocusColor(Paint.valueOf("skyblue"));
            txtPurchasePrice.requestFocus();
        } else {
            txtQtyOnHand.setFocusColor(Paint.valueOf("red"));
            txtQtyOnHand.requestFocus();
        }
    }

    public void txtPurchasePriceOnAction(ActionEvent actionEvent) {
        if (Pattern.compile("^[\\d|.]{1,9}$").matcher(txtPurchasePrice.getText().trim()).matches()) {
            txtPurchasePrice.setFocusColor(Paint.valueOf("skyblue"));
            txtSellPrice.requestFocus();
        } else {
            txtPurchasePrice.setFocusColor(Paint.valueOf("red"));
            txtPurchasePrice.requestFocus();
        }
    }

    public void txtSellPriceOnAction(ActionEvent actionEvent) throws Exception {
        if (Pattern.compile("^[\\d|.]{1,9}$").matcher(txtSellPrice.getText().trim()).matches()) {
            txtSellPrice.setFocusColor(Paint.valueOf("skyblue"));
            btnAdd.requestFocus();
            btnAddOnAction(actionEvent);
        } else {
            txtSellPrice.setFocusColor(Paint.valueOf("red"));
            txtSellPrice.requestFocus();
        }
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        try {
            clear();
            loadCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
