package filter;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import Util.HibernateUtil;
import entidades.Pessoa;

@WebFilter(urlPatterns = { "/*" })
public class FilterAutenticacao extends HttpFilter implements Filter, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HibernateUtil hibernateUtil;

    public FilterAutenticacao() {

    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");
        String url = req.getServletPath();

        if (!url.equalsIgnoreCase("index.xhtml") && usuarioLogado == null) {
            RequestDispatcher rd = req.getRequestDispatcher("/index.xhtml");
            rd.forward(req, response);
            return;
        } else {
            chain.doFilter(req, response);
        }

    }

    public void init(FilterConfig fConfig) throws ServletException {

        hibernateUtil.getEntityManager();

    }

}
