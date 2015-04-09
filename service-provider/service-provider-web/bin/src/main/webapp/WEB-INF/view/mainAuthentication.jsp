<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${pageManager['pleaseAuthenticate']}</title>
</head>
<body>
<a href='/changeLanguage?preferredLanguage=${language}'>${language}</a>
<h1>${pageManager['pleaseAuthenticate']}</h1>
<form action='/authenticateUser' method='post'>
	${pageManager['username']}<input type="text" name='username' />
	${pageManager['password']}<input type="password" name='password' />
	<br>
	<input type="submit" value="${pageManager['submit']}" />	
</form>
</body>
</html>