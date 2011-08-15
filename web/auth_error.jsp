<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <title>Windsor Hum landing page</title>
    <link rel="icon" href="/favicon.ico" type="image/x-icon"/>
</head>
<body>
<h2>An error occurred during authentication</h2>

<p><%= request.getAttribute("error") %>
</p>

<p>Please <a href='<%= request.getAttribute("back_url") %>'>return to the Windsor Hum</a></p>
</body>
</html>