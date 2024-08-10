package dev.vinicius.portfolio.Security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessFilter implements Filter {

    private final JWTTokenProvider jwtTokenProvider;
    private final List<String> exemptedUrls; // URLs que não necessitam de autenticação

    @Autowired
    public AccessFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        // Adicione as URLs que não exigem autenticação
        this.exemptedUrls = List.of("/apis/access/register");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Verifica se a URL da requisição está na lista de URLs isentas
        if (exemptedUrls.contains(path)) {
            chain.doFilter(request, response); // Permite que a requisição prossiga
            return;
        }

        // Obtém o token do cabeçalho Authorization
        String header = req.getHeader("Authorization");

        // Verifica se o cabeçalho tem o prefixo "Bearer "
        String token = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Remove o prefixo "Bearer "
        }

        // Valida o token
        if (token != null && jwtTokenProvider.verifyToken(token)) {
            chain.doFilter(request, response); // Permite que a requisição prossiga se o token for válido
        } else {
            // Configura o status da resposta e a mensagem de erro
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Não autorizado\"}");
        }
    }
}
