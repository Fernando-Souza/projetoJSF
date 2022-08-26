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

import entidades.Cidade;

@Named
@FacesConverter(forClass = Cidade.class, value = "cidadesConverter")
public class CidadesConverter implements Converter, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

        EntityManager manager = CDI.current().select(EntityManager.class).get();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        Cidade cidades = (Cidade) manager.find(Cidade.class, Long.parseLong(codigoCidade));
        transaction.commit();
        return cidades;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return null;
        } else if (value instanceof Cidade) {
            return ((Cidade) value).getId().toString();
        } else {

            return value.toString();

        }
    }

}
