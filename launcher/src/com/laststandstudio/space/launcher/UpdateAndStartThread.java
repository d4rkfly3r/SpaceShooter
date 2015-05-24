package com.laststandstudio.space.launcher;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Freedman on 5/14/2015.
 */
public class UpdateAndStartThread extends Thread {

    @Override
    public void run() {
        try {
            System.out.println(System.getenv("APPDATA"));
            new File(System.getenv("APPDATA") + File.separator + "LastStandStudio" + File.separator + "SpaceShooter").mkdirs();
            //downloadFile("http://direct.mrblockplacer.net/lsnews.txt", System.getenv("APPDATA") + File.separator + "LastStandStudio" + File.separator + "SpaceShooter" + File.separator + "vIndex.xml");

            downloadReqFileds(parseIndexFile());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadReqFileds(List<ReqFile> reqFiles) {
        for (ReqFile reqFile : reqFiles) {
            if (!new File(reqFile.getPath()).exists()) {
                try {
                    downloadFile("http://direct.mrblockplacer.net/ls/v0/files/" + reqFile.getFileName(), reqFile.getPath() + reqFile.getFileName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<ReqFile> parseIndexFile() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(System.getenv("APPDATA") + File.separator + "LastStandStudio" + File.separator + "SpaceShooter" + File.separator + "vIndex.xml"));

            List<ReqFile> requiredFiles = new ArrayList<ReqFile>();
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String identifier = node.getAttributes().getNamedItem("ID").getNodeValue();
                    String name = element.getElementsByTagName("Name").item(0).getChildNodes().item(0).getNodeValue();
                    String version = element.getElementsByTagName("Version").item(0).getChildNodes().item(0).getNodeValue();
                    String type = element.getElementsByTagName("Type").item(0).getChildNodes().item(0).getNodeValue();
                    String path = element.getElementsByTagName("Path").item(0).getChildNodes().item(0).getNodeValue().replace(":>", File.separator);
                    String fileName = element.getElementsByTagName("FileName").item(0).getChildNodes().item(0).getNodeValue();
                    requiredFiles.add(new ReqFile(identifier, name, version, type, path, fileName));
                }
            }


            return requiredFiles;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void downloadFile(String urlString, String saveFileName) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpURLConnection.getHeaderField("Content-Disposition");
            String contentType = httpURLConnection.getContentType();
            int contentLength = httpURLConnection.getContentLength();

            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            } else {
                fileName = urlString.substring(urlString.lastIndexOf("/") + 1, urlString.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("FileName = " + fileName);

            InputStream inputStream = httpURLConnection.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(saveFileName);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
            System.out.println("File Downloaded: " + urlString);
        } else {
            System.out.println("Failed to download: " + urlString);
        }
        httpURLConnection.disconnect();
    }
}