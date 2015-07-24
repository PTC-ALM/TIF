//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.24 at 04:21:50 PM BST 
//


package com.ptc.tifworkbench.jaxbbinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FieldType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="shorttext"/>
 *     &lt;enumeration value="longtext"/>
 *     &lt;enumeration value="siproject"/>
 *     &lt;enumeration value="fva"/>
 *     &lt;enumeration value="pick"/>
 *     &lt;enumeration value="attachment"/>
 *     &lt;enumeration value="logical"/>
 *     &lt;enumeration value="relationship"/>
 *     &lt;enumeration value="user"/>
 *     &lt;enumeration value="group"/>
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="float"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="ibpl"/>
 *     &lt;enumeration value="qbr"/>
 *     &lt;enumeration value="phase"/>
 *     &lt;enumeration value="range"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FieldType")
@XmlEnum
public enum FieldType {

    @XmlEnumValue("shorttext")
    SHORTTEXT("shorttext"),
    @XmlEnumValue("longtext")
    LONGTEXT("longtext"),
    @XmlEnumValue("siproject")
    SIPROJECT("siproject"),
    @XmlEnumValue("fva")
    FVA("fva"),
    @XmlEnumValue("pick")
    PICK("pick"),
    @XmlEnumValue("attachment")
    ATTACHMENT("attachment"),
    @XmlEnumValue("logical")
    LOGICAL("logical"),
    @XmlEnumValue("relationship")
    RELATIONSHIP("relationship"),
    @XmlEnumValue("user")
    USER("user"),
    @XmlEnumValue("group")
    GROUP("group"),
    @XmlEnumValue("integer")
    INTEGER("integer"),
    @XmlEnumValue("float")
    FLOAT("float"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("ibpl")
    IBPL("ibpl"),
    @XmlEnumValue("qbr")
    QBR("qbr"),
    @XmlEnumValue("phase")
    PHASE("phase"),
    @XmlEnumValue("range")
    RANGE("range");
    private final String value;

    FieldType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FieldType fromValue(String v) {
        for (FieldType c: FieldType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}