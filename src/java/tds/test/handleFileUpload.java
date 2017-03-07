/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author grant
 */
public class handleFileUpload extends HttpServlet {

    private static final String FILEPATH = "c:/uploadedFiles/";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String forwardPage = "fileUploadSuccess.jsp";
        String contentType = request.getContentType();
        try {
            // Check that we have a file upload request
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            DataInputStream in = new DataInputStream(request.getInputStream());
            //we are taking the length of Content type data
            int formDataLength = request.getContentLength();
            byte dataBytes[] = new byte[formDataLength];
            int byteRead = 0;
            int totalBytesRead = 0;
            //this loop converting the uploaded file into byte code
            while (totalBytesRead < formDataLength) {
                byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                totalBytesRead += byteRead;
            }

            String file = new String(dataBytes);
            //for saving the file name
            String saveFile = file.substring(file.indexOf("filename=\"") + 10);
            saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
            saveFile = saveFile.substring(saveFile.lastIndexOf("\\")+ 1,saveFile.indexOf("\""));
            int lastIndex = contentType.lastIndexOf("=");
            String boundary = contentType.substring(lastIndex + 1,
contentType.length());
            int pos;
            //extracting the index of file 
            pos = file.indexOf("filename=\"");
            pos = file.indexOf("\n", pos) + 1;
            pos = file.indexOf("\n", pos) + 1;
            pos = file.indexOf("\n", pos) + 1;
            int boundaryLocation = file.indexOf(boundary, pos) - 4;
            int startPos = ((file.substring(0, pos)).getBytes()).length;
            int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

            saveFile = FILEPATH + saveFile;
            System.out.println("Saving file to '" + saveFile + "'");
            // creating a new file with the same name and writing the 
            //content in new file
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            fileOut.write(dataBytes, startPos, (endPos - startPos));
            fileOut.flush();
            fileOut.close();
            
            /*
            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            
            // Process the uploaded items
            for (FileItem item : items) {

                if (item.isFormField()) {
                    // we don't care
                } else {
                    processUploadedFile(item);
                }
            }
            */
        } catch (Exception ex) {
            System.out.println("Error : " + ex.getMessage());
            Logger.getLogger(handleFileUpload.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", ex);
            forwardPage = "fileUploadError.jsp";
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(forwardPage);
        rd.forward(request,response);

    }

    private static void processUploadedFile(FileItem item) {
        try {
            // Process a file upload
            if (!item.isFormField()) {
                String fieldName = item.getFieldName();
                String fileName = item.getName();
                String contentType = item.getContentType();
                boolean isInMemory = item.isInMemory();
                long sizeInBytes = item.getSize();

                File uploadedFile = new File(FILEPATH + fileName);
                item.write(uploadedFile);

            }
        } catch (Exception exception) {
            System.out.println("Error : " + exception.getMessage());
        }
    }
   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "File Upload Servlet";
    }

}
