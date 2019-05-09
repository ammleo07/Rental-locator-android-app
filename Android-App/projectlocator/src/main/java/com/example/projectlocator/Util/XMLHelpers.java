package com.example.projectlocator.Util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Created by alber on 19/11/2018.
 */

public class XMLHelpers {

    public void ObjectToXMLviaJaxB(Object obj,String path)
    {


        try {

            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(obj, file);
            jaxbMarshaller.marshal(obj, System.out);

            System.out.println("object to XML is success");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public Object XMLToObjectviaJaxB(Object obj,String path)
    {
        try {

            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object object = (Object) jaxbUnmarshaller.unmarshal(file);
            return object;

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }

    }
}
