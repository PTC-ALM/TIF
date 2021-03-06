//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.05 at 03:54:57 PM CEST 
//


package com.ptc.tifworkbench.jaxbbinding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="users" type="{http://www.ptc.com/integrity-solution}UsersDefinitions"/>
 *         &lt;element name="fields" type="{http://www.ptc.com/integrity-solution}FieldsDefinitions"/>
 *         &lt;element name="types" type="{http://www.ptc.com/integrity-solution}TypesDefinition"/>
 *         &lt;element name="groups" type="{http://www.ptc.com/integrity-solution}GroupsDefinition"/>
 *         &lt;element name="projects" type="{http://www.ptc.com/integrity-solution}ProjectsDefinitions"/>
 *         &lt;element name="states" type="{http://www.ptc.com/integrity-solution}StatesDefinition"/>
 *         &lt;element name="triggers" type="{http://www.ptc.com/integrity-solution}TriggersDefinition"/>
 *         &lt;element name="queries" type="{http://www.ptc.com/integrity-solution}QueriesDefinition"/>
 *         &lt;element name="reports" type="{http://www.ptc.com/integrity-solution}ReportsDefinition"/>
 *         &lt;element name="dynamic-groups" type="{http://www.ptc.com/integrity-solution}DynamicGroupsDefinition"/>
 *       &lt;/sequence>
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "users",
    "fields",
    "types",
    "groups",
    "projects",
    "states",
    "triggers",
    "queries",
    "reports",
    "dynamicGroups"
})
@XmlRootElement(name = "im-solution")
public class ImSolution
    implements Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected UsersDefinitions users;
    @XmlElement(required = true)
    protected FieldsDefinitions fields;
    @XmlElement(required = true)
    protected TypesDefinition types;
    @XmlElement(required = true)
    protected GroupsDefinition groups;
    @XmlElement(required = true)
    protected ProjectsDefinitions projects;
    @XmlElement(required = true)
    protected StatesDefinition states;
    @XmlElement(required = true)
    protected TriggersDefinition triggers;
    @XmlElement(required = true)
    protected QueriesDefinition queries;
    @XmlElement(required = true)
    protected ReportsDefinition reports;
    @XmlElement(name = "dynamic-groups", required = true)
    protected DynamicGroupsDefinition dynamicGroups;
    @XmlAttribute(name = "title", required = true)
    protected String title;

    /**
     * Gets the value of the users property.
     * 
     * @return
     *     possible object is
     *     {@link UsersDefinitions }
     *     
     */
    public UsersDefinitions getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param value
     *     allowed object is
     *     {@link UsersDefinitions }
     *     
     */
    public void setUsers(UsersDefinitions value) {
        this.users = value;
    }

    /**
     * Gets the value of the fields property.
     * 
     * @return
     *     possible object is
     *     {@link FieldsDefinitions }
     *     
     */
    public FieldsDefinitions getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldsDefinitions }
     *     
     */
    public void setFields(FieldsDefinitions value) {
        this.fields = value;
    }

    /**
     * Gets the value of the types property.
     * 
     * @return
     *     possible object is
     *     {@link TypesDefinition }
     *     
     */
    public TypesDefinition getTypes() {
        return types;
    }

    /**
     * Sets the value of the types property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypesDefinition }
     *     
     */
    public void setTypes(TypesDefinition value) {
        this.types = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link GroupsDefinition }
     *     
     */
    public GroupsDefinition getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupsDefinition }
     *     
     */
    public void setGroups(GroupsDefinition value) {
        this.groups = value;
    }

    /**
     * Gets the value of the projects property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectsDefinitions }
     *     
     */
    public ProjectsDefinitions getProjects() {
        return projects;
    }

    /**
     * Sets the value of the projects property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectsDefinitions }
     *     
     */
    public void setProjects(ProjectsDefinitions value) {
        this.projects = value;
    }

    /**
     * Gets the value of the states property.
     * 
     * @return
     *     possible object is
     *     {@link StatesDefinition }
     *     
     */
    public StatesDefinition getStates() {
        return states;
    }

    /**
     * Sets the value of the states property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatesDefinition }
     *     
     */
    public void setStates(StatesDefinition value) {
        this.states = value;
    }

    /**
     * Gets the value of the triggers property.
     * 
     * @return
     *     possible object is
     *     {@link TriggersDefinition }
     *     
     */
    public TriggersDefinition getTriggers() {
        return triggers;
    }

    /**
     * Sets the value of the triggers property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriggersDefinition }
     *     
     */
    public void setTriggers(TriggersDefinition value) {
        this.triggers = value;
    }

    /**
     * Gets the value of the queries property.
     * 
     * @return
     *     possible object is
     *     {@link QueriesDefinition }
     *     
     */
    public QueriesDefinition getQueries() {
        return queries;
    }

    /**
     * Sets the value of the queries property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueriesDefinition }
     *     
     */
    public void setQueries(QueriesDefinition value) {
        this.queries = value;
    }

    /**
     * Gets the value of the reports property.
     * 
     * @return
     *     possible object is
     *     {@link ReportsDefinition }
     *     
     */
    public ReportsDefinition getReports() {
        return reports;
    }

    /**
     * Sets the value of the reports property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportsDefinition }
     *     
     */
    public void setReports(ReportsDefinition value) {
        this.reports = value;
    }

    /**
     * Gets the value of the dynamicGroups property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicGroupsDefinition }
     *     
     */
    public DynamicGroupsDefinition getDynamicGroups() {
        return dynamicGroups;
    }

    /**
     * Sets the value of the dynamicGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicGroupsDefinition }
     *     
     */
    public void setDynamicGroups(DynamicGroupsDefinition value) {
        this.dynamicGroups = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ImSolution)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ImSolution that = ((ImSolution) object);
        {
            UsersDefinitions lhsUsers;
            lhsUsers = this.getUsers();
            UsersDefinitions rhsUsers;
            rhsUsers = that.getUsers();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "users", lhsUsers), LocatorUtils.property(thatLocator, "users", rhsUsers), lhsUsers, rhsUsers)) {
                return false;
            }
        }
        {
            FieldsDefinitions lhsFields;
            lhsFields = this.getFields();
            FieldsDefinitions rhsFields;
            rhsFields = that.getFields();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fields", lhsFields), LocatorUtils.property(thatLocator, "fields", rhsFields), lhsFields, rhsFields)) {
                return false;
            }
        }
        {
            TypesDefinition lhsTypes;
            lhsTypes = this.getTypes();
            TypesDefinition rhsTypes;
            rhsTypes = that.getTypes();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
                return false;
            }
        }
        {
            GroupsDefinition lhsGroups;
            lhsGroups = this.getGroups();
            GroupsDefinition rhsGroups;
            rhsGroups = that.getGroups();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "groups", lhsGroups), LocatorUtils.property(thatLocator, "groups", rhsGroups), lhsGroups, rhsGroups)) {
                return false;
            }
        }
        {
            ProjectsDefinitions lhsProjects;
            lhsProjects = this.getProjects();
            ProjectsDefinitions rhsProjects;
            rhsProjects = that.getProjects();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "projects", lhsProjects), LocatorUtils.property(thatLocator, "projects", rhsProjects), lhsProjects, rhsProjects)) {
                return false;
            }
        }
        {
            StatesDefinition lhsStates;
            lhsStates = this.getStates();
            StatesDefinition rhsStates;
            rhsStates = that.getStates();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "states", lhsStates), LocatorUtils.property(thatLocator, "states", rhsStates), lhsStates, rhsStates)) {
                return false;
            }
        }
        {
            TriggersDefinition lhsTriggers;
            lhsTriggers = this.getTriggers();
            TriggersDefinition rhsTriggers;
            rhsTriggers = that.getTriggers();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "triggers", lhsTriggers), LocatorUtils.property(thatLocator, "triggers", rhsTriggers), lhsTriggers, rhsTriggers)) {
                return false;
            }
        }
        {
            QueriesDefinition lhsQueries;
            lhsQueries = this.getQueries();
            QueriesDefinition rhsQueries;
            rhsQueries = that.getQueries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "queries", lhsQueries), LocatorUtils.property(thatLocator, "queries", rhsQueries), lhsQueries, rhsQueries)) {
                return false;
            }
        }
        {
            ReportsDefinition lhsReports;
            lhsReports = this.getReports();
            ReportsDefinition rhsReports;
            rhsReports = that.getReports();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "reports", lhsReports), LocatorUtils.property(thatLocator, "reports", rhsReports), lhsReports, rhsReports)) {
                return false;
            }
        }
        {
            DynamicGroupsDefinition lhsDynamicGroups;
            lhsDynamicGroups = this.getDynamicGroups();
            DynamicGroupsDefinition rhsDynamicGroups;
            rhsDynamicGroups = that.getDynamicGroups();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dynamicGroups", lhsDynamicGroups), LocatorUtils.property(thatLocator, "dynamicGroups", rhsDynamicGroups), lhsDynamicGroups, rhsDynamicGroups)) {
                return false;
            }
        }
        {
            String lhsTitle;
            lhsTitle = this.getTitle();
            String rhsTitle;
            rhsTitle = that.getTitle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "title", lhsTitle), LocatorUtils.property(thatLocator, "title", rhsTitle), lhsTitle, rhsTitle)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            UsersDefinitions theUsers;
            theUsers = this.getUsers();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "users", theUsers), currentHashCode, theUsers);
        }
        {
            FieldsDefinitions theFields;
            theFields = this.getFields();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fields", theFields), currentHashCode, theFields);
        }
        {
            TypesDefinition theTypes;
            theTypes = this.getTypes();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
        }
        {
            GroupsDefinition theGroups;
            theGroups = this.getGroups();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "groups", theGroups), currentHashCode, theGroups);
        }
        {
            ProjectsDefinitions theProjects;
            theProjects = this.getProjects();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "projects", theProjects), currentHashCode, theProjects);
        }
        {
            StatesDefinition theStates;
            theStates = this.getStates();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "states", theStates), currentHashCode, theStates);
        }
        {
            TriggersDefinition theTriggers;
            theTriggers = this.getTriggers();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "triggers", theTriggers), currentHashCode, theTriggers);
        }
        {
            QueriesDefinition theQueries;
            theQueries = this.getQueries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "queries", theQueries), currentHashCode, theQueries);
        }
        {
            ReportsDefinition theReports;
            theReports = this.getReports();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "reports", theReports), currentHashCode, theReports);
        }
        {
            DynamicGroupsDefinition theDynamicGroups;
            theDynamicGroups = this.getDynamicGroups();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dynamicGroups", theDynamicGroups), currentHashCode, theDynamicGroups);
        }
        {
            String theTitle;
            theTitle = this.getTitle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "title", theTitle), currentHashCode, theTitle);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            UsersDefinitions theUsers;
            theUsers = this.getUsers();
            strategy.appendField(locator, this, "users", buffer, theUsers);
        }
        {
            FieldsDefinitions theFields;
            theFields = this.getFields();
            strategy.appendField(locator, this, "fields", buffer, theFields);
        }
        {
            TypesDefinition theTypes;
            theTypes = this.getTypes();
            strategy.appendField(locator, this, "types", buffer, theTypes);
        }
        {
            GroupsDefinition theGroups;
            theGroups = this.getGroups();
            strategy.appendField(locator, this, "groups", buffer, theGroups);
        }
        {
            ProjectsDefinitions theProjects;
            theProjects = this.getProjects();
            strategy.appendField(locator, this, "projects", buffer, theProjects);
        }
        {
            StatesDefinition theStates;
            theStates = this.getStates();
            strategy.appendField(locator, this, "states", buffer, theStates);
        }
        {
            TriggersDefinition theTriggers;
            theTriggers = this.getTriggers();
            strategy.appendField(locator, this, "triggers", buffer, theTriggers);
        }
        {
            QueriesDefinition theQueries;
            theQueries = this.getQueries();
            strategy.appendField(locator, this, "queries", buffer, theQueries);
        }
        {
            ReportsDefinition theReports;
            theReports = this.getReports();
            strategy.appendField(locator, this, "reports", buffer, theReports);
        }
        {
            DynamicGroupsDefinition theDynamicGroups;
            theDynamicGroups = this.getDynamicGroups();
            strategy.appendField(locator, this, "dynamicGroups", buffer, theDynamicGroups);
        }
        {
            String theTitle;
            theTitle = this.getTitle();
            strategy.appendField(locator, this, "title", buffer, theTitle);
        }
        return buffer;
    }

}
