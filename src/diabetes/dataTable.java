/*
    CLASE ABSTRACTA PARA IMPRIMIR LOS DATOS EN LA TABLA.
    USO DE MÉTODOS ENCAPSULADOS Y MÉTODO toString.
*/

package diabetes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Luis Alfonso Glez
 */
public class dataTable {
    private final StringProperty idGlu;
    private final StringProperty edGlu;
    private final StringProperty vaGlu;
    private final StringProperty esGlu;
    private final StringProperty diaGlu;
    
    dataTable(){
        idGlu = new SimpleStringProperty(this, "idGlu");
        edGlu = new SimpleStringProperty(this, "edGlu");
        vaGlu = new SimpleStringProperty(this, "vaGlu");
        esGlu = new SimpleStringProperty(this, "esGlu");
        diaGlu = new SimpleStringProperty(this, "diaGlu");
    }
    
    //Id
    StringProperty idGluProperty(){
        return idGlu;
    }
    String getIdGlu(){
        return idGlu.get();
    }
    void setIdGlu(String id){
        idGlu.set(id);
    }
    
    //edad
    StringProperty edGluProperty(){
        return edGlu;
    }
    String getEdGlu(){
        return edGlu.get();
    }
    void setEdGlu(String ed){
        edGlu.set(ed);
    }
    
    //valor (nivel de azucar)
    StringProperty vaGluProperty(){
        return vaGlu;
    }
    String getVaGlu(){
        return vaGlu.get();
    }
    void setVaGlu(String va){
        vaGlu.set(va);
    }
    
    //Estado
    StringProperty esGluProperty(){
        return esGlu;
    }
    String getEsGlu(){
        return esGlu.get();
    }
    void setEsGlu(String es){
        esGlu.set(es);
    }
    
    //diagnostico
    StringProperty diaGluProperty(){
        return diaGlu;
    }
    String getDiaGlu(){
        return diaGlu.get();
    }
    void setDiaGlu(String dia){
        diaGlu.set(dia);
    }
    
    @Override
    public String toString(){
        return String.format("%s[idGlu=%s, edGlu=%s]", getIdGlu(), getEdGlu(), getVaGlu(), getEsGlu(), getDiaGlu());
    }
   
}
