package converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;
import entidades.Estado;

@FacesConverter(forClass = Estado.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {

        EntityManager manager = HibernateUtil.getEntityManager();
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
