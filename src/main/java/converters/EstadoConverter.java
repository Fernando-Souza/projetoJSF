package converters;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import entidades.Estado;

@Named
@FacesConverter(forClass = Estado.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {

        EntityManager manager = CDI.current().select(EntityManager.class).get();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        Estado estados = (Estado) manager.find(Estado.class, Long.parseLong(codigoEstado));
        transaction.commit();
        return estados;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return null;
        } else if (value instanceof Estado) {
            return ((Estado) value).getId().toString();
        } else {

            return value.toString();

        }

    }

}
