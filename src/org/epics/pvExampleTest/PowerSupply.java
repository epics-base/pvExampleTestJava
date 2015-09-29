package org.epics.pvExampleTest;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.factory.StandardFieldFactory;
import org.epics.pvdata.property.Alarm;
import org.epics.pvdata.property.AlarmSeverity;
import org.epics.pvdata.property.PVAlarm;
import org.epics.pvdata.property.PVAlarmFactory;
import org.epics.pvdata.property.PVTimeStamp;
import org.epics.pvdata.property.PVTimeStampFactory;
import org.epics.pvdata.property.TimeStamp;
import org.epics.pvdata.property.TimeStampFactory;
import org.epics.pvdata.pv.FieldBuilder;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.StandardField;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdatabase.PVRecord;

public class PowerSupply extends PVRecord {
    
    private PVDouble pvCurrent =  null;
    private PVDouble pvPower =  null;
    private PVDouble pvVoltage =  null;
    private PVAlarm pvAlarm =  PVAlarmFactory.create();
    private PVTimeStamp pvTimeStamp =  PVTimeStampFactory.create();
    private Alarm alarm = new Alarm();
    private TimeStamp timeStamp = TimeStampFactory.create();
    
    public static PVRecord create(String recordName)
    {
        FieldCreate fieldCreate = FieldFactory.getFieldCreate();
        PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
        StandardField standardField = StandardFieldFactory.getStandardField();
        
        
        FieldBuilder fb = fieldCreate.createFieldBuilder();
        Structure structure = 
                fb.add("alarm",standardField.alarm()). 
                add("timeStamp",standardField.timeStamp()).
                addNestedStructure("power").
                   add("value",ScalarType.pvDouble).
                   add("alarm",standardField.timeStamp()).
                   endNested().
                addNestedStructure("voltage").
                   add("value",ScalarType.pvDouble).
                   add("alarm",standardField.timeStamp()).
                   endNested().
                addNestedStructure("current").
                   add("value",ScalarType.pvDouble).
                   add("alarm",standardField.timeStamp()).
                   endNested().
                createStructure();
       return new PowerSupply(recordName,pvDataCreate.createPVStructure(structure));
    }
    
    public PowerSupply(String recordName,PVStructure pvStructure) {
        super(recordName,pvStructure);        
        PVField pvField;
        boolean result;
        pvField = pvStructure.getSubField("timeStamp");
        if(pvField==null) {
            throw new IllegalArgumentException("no timeStamp");
        }
        result = pvTimeStamp.attach(pvField);
        if(!result) {
            throw new IllegalArgumentException("no timeStamp");
        }
        pvField = pvStructure.getSubField("alarm");
        if(pvField==null) {
            throw new IllegalArgumentException("no alarm");
        }
        result = pvAlarm.attach(pvField);
        if(!result) {
            throw new IllegalArgumentException("no alarm");
           
        }
        pvCurrent = pvStructure.getSubField(PVDouble.class,"current.value");
        if(pvCurrent==null) {
            throw new IllegalArgumentException("no current");
        }
        pvVoltage = pvStructure.getSubField(PVDouble.class,"voltage.value");
        if(pvVoltage==null) {
            throw new IllegalArgumentException("no voltage");
        }
        pvPower = pvStructure.getSubField(PVDouble.class,"power.value");
        if(pvPower==null) {
            throw new IllegalArgumentException("no power");
        }
    }
    public void process()
    {
        timeStamp.getCurrentTime();
        pvTimeStamp.set(timeStamp);
        double voltage = pvVoltage.get();
        double power = pvPower.get();
        if(voltage<1e-3 && voltage>-1e-3) {
            alarm.setMessage("bad voltage");
            alarm.setSeverity(AlarmSeverity.MAJOR);
            pvAlarm.set(alarm);
            return;
        }
        double current = power/voltage;
        pvCurrent.put(current);
        alarm.setMessage("");
        alarm.setSeverity(AlarmSeverity.NONE);
        pvAlarm.set(alarm);
        super.process();
        
        
    }
}
