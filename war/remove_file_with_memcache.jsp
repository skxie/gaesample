<%@ page language="java" pageEncoding="utf-8"%>  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    
    <title>remove file with memcache</title>  
  </head>  
    <body>  
        <h3>Remove File with Memcache</h3>  
        <hr />  
        <form action="removefilemem" method="post">  
            <table>  
                <tr>  
                    <td>  
                        <p>File Name</p>
                    </td>  
                    <td>  
                        <input type="text" id="filename" name="filename"/>  
                    </td>  
                </tr>  
                <tr>  
                    <td>  
                           
                    </td>  
                    <td>  
                        <input type="submit" value="Remove File" />  
                    </td>  
                </tr>  
            </table>  
        </form>  
    </body>  
</html> 