<%@ page language="java" pageEncoding="utf-8"%>  
  
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    <base href="<%=basePath%>">  
    <title>file_upload</title>  
  </head>  
    <body>  
        <h3>File Upload</h3>  
        <hr />  
        <form action="fileUpload" method="post" enctype="multipart/form-data" >  
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
                        <input type="submit" value="Upload File" />  
                    </td>  
                </tr>  
            </table>  
        </form>  
    </body>  
</html> 