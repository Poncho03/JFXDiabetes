/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabetes;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Luis Alfonso Glez
 */
public class FXMLDocumentController implements Initializable {

    /* SE DECLARAN LOS OBJETOS QUE SE UTLIZAN EN EL LADO DE LA INTERFAZ */
    @FXML
    private TableView<dataTable> tableData;

    @FXML
    private TableColumn<dataTable, String> idCol;

    @FXML
    private TableColumn<dataTable, String> edadCol;

    @FXML
    private TableColumn<dataTable, String> azucarCol;
    
    @FXML
    private TableColumn<dataTable, String> esCol;

    @FXML
    private TableColumn<dataTable, String> diagCol;

    @FXML
    private TextField txtAzucar;

    @FXML
    private TextField txtEdad;

    @FXML
    private Pane panelStandby;

    @FXML
    private Pane panelData;

    @FXML
    private Label labelEstado;

    @FXML
    private Label labelAzucar;

    @FXML
    private Label labelEdad;

    @FXML
    private Label labelDiag;
    
    /* SE CREAN LAS VARIABLES PARA EL USO DE SQL */
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    /* SE CREA EL MÉTODO QUE HACE LA CONEXIÓN CON LA BASE DE DATOS */
    void Connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/diabetes","root","");
        }
        catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* EVENTO DEL BOTÓN QUE GUARDA LOS DATOS EN LA BASE */
    @FXML
    void saveData(ActionEvent event) {
        
        if(txtAzucar.getText().compareTo("")!=0 && txtEdad.getText().compareTo("")!=0){
            if(Integer.parseInt(txtEdad.getText())<110){
                Connect();
            
                int azucar = Integer.parseInt(txtAzucar.getText());
                int edad = Integer.parseInt(txtEdad.getText());
                String estado = "";
                String diag = "";

                if(azucar <= 75){
                    estado = "Hipoglucemia";
                    diag = "Sus niveles de azúcar están por debajo del recomendado, si usted no tiene diabetes le recomendamos buscar atención medica urgentemente.";
                }
                if(azucar>=76 && azucar<=135){
                    estado = "Normal";
                    diag = "¡Muy bien! Sus niveles se encuentran en los parámetros adecuados. Aún así le aconsejamos que se mantenga activo y coma saludablemente.";
                }
                if(azucar>=136 && azucar<=195){
                    estado = "Prediabetes";
                    diag = "Sus niveles de azúcar no son muy buenos, aún está a tiempo, inicie una vida más saludable y consulte a su medico regularmente.";
                }
                if(azucar>=196){
                    estado = "Crítico";
                    diag = "Usted se encuentra en riesgo de padecer severas complicaciones, como ceguera o un ataque cardiaco, ¡necesita atención urgentemente!";
                }
                try{
                    pst = con.prepareStatement("INSERT INTO diagnosticos(edGlu, vaGlu, esGlu, diaGlu) values (?,?,?,?)");
                    pst.setInt(1, edad);
                    pst.setInt(2, azucar);
                    pst.setString(3, estado);
                    pst.setString(4, diag);
                    int status = pst.executeUpdate();

                    if(status == 1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Completado");
                        alert.setHeaderText("El diagnostico se realizó con éxito");
                        alert.showAndWait();

                        txtAzucar.setText("");
                        txtEdad.setText("");

                        panelStandby.setVisible(false);
                        labelEstado.setText(estado);
                        labelAzucar.setText(String.valueOf(azucar));
                        labelEdad.setText(String.valueOf(edad));
                        labelDiag.setText(diag);
                        panelData.setVisible(true);
                        table();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Oh no...");
                        alert.setHeaderText("Parece que tenemos problemas para evaluar su estado, intente más tarde.");
                        alert.showAndWait();
                    }
                }
                catch(SQLException e){
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("¿Su edad es correcta?");
                alert.setContentText("Si la edad que ingresó es correcta, primero permitanos felicitarlo por llegar tan lejos, no hace falta un diagnostico, usted debe de checarse constantemente.");
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Por favor, ingrese los datos que se le solicitan para poder evaluar su estado de salud.");
            alert.showAndWait();
        }
    }
    
    /* BOTÓN QUE LIMPIA EL DIAGNOSTICO OBTENIDO PARA PODER HACER UNO NUEVO */
    @FXML
    void reset(ActionEvent event) {
        panelStandby.setVisible(true);
        labelEstado.setText("");
        labelAzucar.setText("");
        labelEdad.setText("");
        labelDiag.setText("");
        panelData.setVisible(false);
    }
    
    /* MÉTODO QUE OBTIENE LOS DATOS Y LOS IMPRIME EN LA TABLA */
    /* SE APOYA DE LA CLASE dataTable PARA EL USO DE LOS MÉTODOS SET Y GET */
    void table(){
        ObservableList<dataTable> dataTable = FXCollections.observableArrayList();
        try{
            pst = con.prepareStatement("SELECT * FROM diagnosticos");
            rs = pst.executeQuery();
            
            while(rs.next()){
                dataTable dt = new dataTable();
                dt.setIdGlu(rs.getString("idGlu"));
                dt.setEdGlu(rs.getString("edGlu"));
                dt.setVaGlu(rs.getString("vaGlu"));
                dt.setEsGlu(rs.getString("esGlu"));
                dt.setDiaGlu(rs.getString("diaGlu"));
                dataTable.add(dt);
            }
            
            tableData.setItems(dataTable);
            idCol.setCellValueFactory(f -> f.getValue().idGluProperty());
            edadCol.setCellValueFactory(f -> f.getValue().edGluProperty());
            azucarCol.setCellValueFactory(f -> f.getValue().vaGluProperty());
            esCol.setCellValueFactory(f -> f.getValue().esGluProperty());
            diagCol.setCellValueFactory(f -> f.getValue().diaGluProperty());
        }
        catch(SQLException e){
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /* MÉTODO DE INICIALIZACIÓN */
    /* SE LLAMAN LOS MÉTODOS DE CONEXIÓN Y DE LA TABLA */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        table();
    }    
    
}
