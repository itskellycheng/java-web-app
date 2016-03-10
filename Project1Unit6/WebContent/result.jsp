<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="java.util.*"%>
<%@ page import="adapter.BuildAuto" %>
<!DOCTYPE html>
<%
	BuildAuto myCar = new BuildAuto();
    double totalCost = 0;
	String modelName = request.getParameter("modelName");
	
	System.out.println(modelName);
	
	/* Get selections and save them */
	String transmission = request.getParameter("Transmission");
	String brakes = request.getParameter("Brakes");
	myCar.saveOptionChoice(modelName, "Transmission", transmission);
	myCar.saveOptionChoice(modelName, "Brakes", brakes);
    
	HashMap<String, Float> prices = new HashMap();
    prices = myCar.getPrices(modelName);
    
    //String basePrice = Float.toString(prices.get("baseprice"));
    Float basePrice = prices.get("baseprice");
    Float totalPrice = prices.get("totalprice");
    
    /*get transmission and price*/
    
    Float transmissionPrice = prices.get("Transmission");
    
    /*get brakes and price*/
    
    Float brakesPrice = prices.get("Brakes");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Result</title>
</head>
<body>
    <CENTER>
        <h3>Here is what you selected:</h3>
        <table>
            <tr>
                <td><%=modelName%></td>
                <td>Base Price</td>
                <td><%=basePrice%></td>
            </tr>
            <tr>
                <td>Transmission</td>
                <td><%=transmission%></td>
                <td><%=transmissionPrice%></td>
            </tr>
            <tr>
                <td>Brakes/Traction Control</td>
                <td><%=brakes%></td>
                <td><%=brakesPrice%></td>
            </tr>
            <tr>
                <td><b>Total Cost</b></td>
                <td></td>
                <td><%=totalPrice%></td>
            </tr>
        </table>
    </CENTER>
</body>
</html>