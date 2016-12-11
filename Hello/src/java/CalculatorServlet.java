/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author xxx
 */
public class CalculatorServlet extends HttpServlet {

    HttpSession session;
    String lastOperation;
    
    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        this.lastOperation = "";
    }

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CalculatorServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            
            this.session = request.getSession();
            
            if (this.isClearSessionRequest(request)) {
                if (this.session != null) {
                    this.session.removeAttribute("lastOperations");
                    out.println("<p>Sesja wyczyszczona! <a href=\"http://localhost:8080/Hello/Calculator.html\">Wroc do kalkulatora</a></p>");
                }
            }
            
            String result = "<p>" + this.calculationResult(request) + "</p>";
            if (this.isFormValid(request)) {
                out.println("<h1>Wynik operacji</h1>");
                
                out.println(result); 
            } else {
                out.println("<p>Invalid intput!!!</p>");
            }
            out.println("<h1>Historia operacji</h1>");
                
            this.lastOperation = result + (String)this.session.getAttribute("lastOperations");
            if (lastOperation == null) {
                out.println("<p>Brak historii</p>");
            } else {
                out.println(lastOperation);                
            }
            if (this.isFormValid(request)) {
                lastOperation += result; 
                this.session.setAttribute("lastOperations", lastOperation);
            }
                
            out.println("<p><a href=\"http://localhost:8080/Hello/Calculator.html\">Nowa operacja</a></p>");
            out.println(
                "<form action=\"CalculatorServlet\" method=\"post\">\n" +
                "  <button type=\"submit\" name=\"clearSession\" value=\"clearSession\">Wyczysc sesje</button>\n" +
                "</form>"
            );
            
            //out.println("<h1>Servlet CalculatorServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private String calculationResult(HttpServletRequest request) {
        double firstNumber = Double.parseDouble(request.getParameter("firstNumber"));
        double secondNumber = Double.parseDouble(request.getParameter("secondNumber"));
        String operation = request.getParameter("operation");
        double result = this.calculate(firstNumber, secondNumber, operation);
        
        return firstNumber + " " + operation + " " + secondNumber + " = " + this.calculate(firstNumber, secondNumber, operation);
        //return "";
    }
    
    private double calculate(double firstNumber, double secondNumber, String operation) {
        switch (operation) {
            case "+":
                return firstNumber + secondNumber;
            case "-":
                return firstNumber - secondNumber;
            case "*":
                return firstNumber * secondNumber;
            case "/":
                return firstNumber / secondNumber;
            default:
                 return 0;
        }
    }
    
    private boolean isClearSessionRequest(HttpServletRequest request) {
        String param = request.getParameter("clearSession");
        return param != null;
    }
    
    private boolean isFormValid(HttpServletRequest request) {
        return !request.getParameter("firstNumber").equals("") &&
                !request.getParameter("secondNumber").equals("") &&
                request.getParameter("firstNumber") != null &&
                request.getParameter("secondNumber") != null &&
                this.isNumeric(request.getParameter("firstNumber")) &&
                this.isNumeric(request.getParameter("secondNumber"));
    }
    
    private boolean isNumeric(String s) {  
        return s.matches("[-+]?\\d*\\.?\\d+");  
    } 
    
    public boolean isValid(String input, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher seq = pattern.matcher(input);
        return seq.find();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
