/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Xpath;

import java.io.File;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Ruben
 */
public class Gestionar_Xpath {
//Objeto Document que almacena el DOM del XML seleccionado.

    Document doc;

    XPath xpath;

    public int abrir_XML(File fichero) {
        //doc representará el árbol DOM
        doc = null;

        try {
            //Se crea un objeto DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //Indica que el modelo DOM no debe contemplar los comentarios que tenga el XML
            factory.setIgnoringComments(true);
            //Ignora los espacios en blanco que tenga el objeto
            factory.setIgnoringElementContentWhitespace(true);
            //Se crea un objeto DocumentBuilder para cargar en él la estructura de árbol DOM a partir del XML seleccionado
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Interpreta (parsear) el documento XML (file) y genera el DOM equivalente
            doc = builder.parse(fichero);
            //Ahora doc apunta al árbol DOM listo para ser recorrido

            xpath = XPathFactory.newInstance().newXPath();
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public String Ejecutar_XPath(String consulta) {

        String salida = "";
        Node node;
        String datos_nodo[] = null;

        try {

            XPathExpression exp = xpath.compile(consulta);

            Object resultado = exp.evaluate(doc, XPathConstants.NODESET);
            NodeList listaNodos = (NodeList) resultado;

            Node nodoRecibido = (Node) exp.evaluate(doc, XPathConstants.NODE);

            if (nodoRecibido.getNodeName() == "Libro") {
                for (int i = 0; i < listaNodos.getLength(); i++) {
                    node = listaNodos.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        datos_nodo = procesarLibro(node);
                        salida += "\r\n " + "Publicado en: " + datos_nodo[0];
                        salida += "\r\n " + "El título es: " + datos_nodo[1];
                        salida += "\r\n " + "El autor es: " + datos_nodo[2];
                        salida += "\r\n " + "La editorial es: " + datos_nodo[3];
                        salida += "\r\n -------------------------";
                    }
                }
            } else if (nodoRecibido.getNodeName() == "Autor" || nodoRecibido.getNodeName() == "Titulo" || nodoRecibido.getNodeName() == "Editorial") {
                for (int j = 0; j < listaNodos.getLength(); j++) {
                    salida += "\n" + listaNodos.item(j).getFirstChild().getNodeValue();
                }
            } else {
                for (int n = 0; n < listaNodos.getLength(); n++) {
                    salida += "\n" + listaNodos.item(n).getNodeValue();
                }
            }
            return salida;
        } catch (Exception e) {
            return "Error en la ejecución de la salida";
        }
    }

    private String[] procesarLibro(Node _n) {
        String libros[] = new String[4];
        Node hijosLibro = null;
        int contador = 1;

        //Obtiene el valor del primer atributo del nodo
        libros[0] = _n.getAttributes().item(0).getNodeValue();

        NodeList nodos = _n.getChildNodes();

        for (int i = 0; i < nodos.getLength(); i++) {
            hijosLibro = nodos.item(i);

            if (hijosLibro.getNodeType() == Node.ELEMENT_NODE) {
                //Para acceder al texto con el título y autor 
                //accedo al nodo text hijo de ntemp y se saca su valor.
                libros[contador] = hijosLibro.getFirstChild().getNodeValue();
                contador++;
            }
        }

        return libros;
    }
}
