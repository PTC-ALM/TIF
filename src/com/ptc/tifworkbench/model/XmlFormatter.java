/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author pbowden
 */
public class XmlFormatter 
{
    private static Marshaller marshaller = null;
    private static Unmarshaller unmarshaller = null;

    static 
    {
        try 
        {
            JAXBContext context = JAXBContext.newInstance("com.ptc.tifworkbench.jaxbbinding");
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("There was a problem creating a JAXBContext object for formatting the object to XML.");
        }
    }

    public void marshal(ImSolution sol, OutputStream os) throws JAXBException 
    {
        marshaller.marshal(sol, os);
    }

    public String marshalToString(ImSolution sol) throws JAXBException 
    {
        StringWriter sw = new StringWriter();
        marshaller.marshal(sol, sw);
        return sw.toString();
    }
}
