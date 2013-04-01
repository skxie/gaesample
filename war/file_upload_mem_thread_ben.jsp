<%@ page language="java" pageEncoding="utf-8"%>  
  
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    <base href="<%=basePath%>">  
    <title>file_upload with Memcache and Threaded</title>  
  </head>  
    <body>  
        <h3>File Upload with Memcache and Threaded</h3>  
        <hr />  
        <form action="fileuploadmemthreadedben" method="post" enctype="multipart/form-data" >  
            <table>  
                <tr>  
                    <td>  
                           
                    </td>  
                    <td>  
                        <input type="file" name="file" multiple="multiple"/>  
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