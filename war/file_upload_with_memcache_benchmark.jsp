<%@ page language="java" pageEncoding="utf-8"%>  
  
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    <base href="<%=basePath%>">  
    <title>file_upload with memcache</title>  
  </head>  
    <body>  
        <h3>File Upload with Memcache</h3>  
        <hr />  
        <form action="fileuploadmemben" method="post" enctype="multipart/form-data" >  
            <table>  
                <tr>  
                    <td>  
                           
                    </td>  
                    <td>  
                        <input type="file" name="file" multiple="true"/>  
                    </td>  
                </tr>  
                <tr>  
                    <td>  
                           
                    </td>  
                    <td>  
                        <input type="submit" value="Find File" />  
                    </td>  
                </tr>  
            </table>  
        </form>  
    </body>  
</html> 