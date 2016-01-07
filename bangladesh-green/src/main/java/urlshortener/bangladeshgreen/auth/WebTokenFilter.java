package urlshortener.bangladeshgreen.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.filter.GenericFilterBean;
import urlshortener.bangladeshgreen.domain.messages.ErrorResponse;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This filter checks if authorization header is present in an HTTP request.
 * If so, checks if the JSON Web Token (JWT) is correct and not expired.
 * If there is an error, an error message is returned. Else, the filter chain continues.
 * NOTE: Only executed for protected paths (View Application.java)
 */
@Configurable
public class WebTokenFilter extends GenericFilterBean {

    private String key;

    private ArrayList<URLProtection> toProtect;

    /**
     * Constructor of servlet filter.
     * @param key is the secret key for signing.
     */
    public WebTokenFilter(String key){
        this.key = key;
        toProtect = new ArrayList<>();
    }

    public void addUrlToProtect(URLProtection p){
        this.toProtect.add(p);
    }





    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req; //Request
        final HttpServletResponse response  = (HttpServletResponse) res; //Response


        //Requires authentication
        if(requiresAuthentication(request)){



            if (getTokenFromCookies(request) == null) {
                //No authentication in the request
                sendErrorResponse(request,response,
                        "Authorization error: No token is supplied. Please obtain one from /login.");
            }

            else{
                //Authentication in the request
                final String token = getTokenFromCookies(request);
                try {
                    //Parse claims from JWT
                    final Claims claims = Jwts.parser().setSigningKey(key)
                            .parseClaimsJws(token).getBody();

                        //Correct token -> User is logged-in
                        request.setAttribute("claims",claims);
                        chain.doFilter(req, res); //Continue with filters

                }
                catch(ExpiredJwtException expiredException){
                    sendErrorResponse(request,response,
                            "Authorization error: " + expiredException.getMessage());
                }
                catch (final SignatureException  | NullPointerException  |MalformedJwtException ex) {
                    sendErrorResponse(request,response,
                            "Authorization error: Invalid token format. Please obtain a new token from /login");
                }


            }
        }
        else{

            //Does not require authentication, but if a valid token is supplied, we pass it to the controller
            //(For example, for being able to handle authentication-required URLs)
            if(getTokenFromCookies(request) != null) {
                //Authentication in the request
                final String token = getTokenFromCookies(request);
                try {
                    //Parse claims from JWT
                    final Claims claims = Jwts.parser().setSigningKey(key)
                            .parseClaimsJws(token).getBody();

                    //Correct token -> User is logged-in
                    req.setAttribute("claims", claims);

                    chain.doFilter(req,res);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    chain.doFilter(req,res);
                    //Nothing
                }
            }
            else{

                chain.doFilter(request,response);
            }


        }

    }



    /**
     * Returns true if the URL requires authentication.
     */
    public boolean requiresAuthentication(HttpServletRequest request){
        String destinationURL = request.getRequestURI();


        //Check every URL to protect
        for(URLProtection url: toProtect){
            Pattern p = Pattern.compile(url.getUrl());



            Matcher m = p.matcher(destinationURL);
            if(m.matches()){ //A filter has been found for that URL
                //Check method

                if(url.hasMethod(request.getMethod())){
                    //It has a method that needs to be authenticated

                    return true;
                }
            }

        }
        return false;
    }

    public String extractToken(String authHeader){
        return authHeader.substring(7);

    }
    /**
     * Writes to the response an error message.
     * If accept header contains "html", a forward to 401.jsp is made.
     * Else, JSON is returned.
     */
    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,String message) throws IOException{


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if(request.getHeader("Accept")!=null && request.getHeader("Accept").contains("html")){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/401.jsp");
            try {
                dispatcher.forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        else{
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(message);
            ObjectMapper mapper = new ObjectMapper();


            response.setContentType("application/json");
            response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        }



    }

    private String getTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase("wallaclaim")) {
                    return c.getValue();
                }

            }
        }
        return null;

    }

}
