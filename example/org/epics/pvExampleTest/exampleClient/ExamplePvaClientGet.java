/*ExamplePvaClientGet.java */
/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS pvData is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
/**
 * @author mrk
 */

/* Author: Marty Kraimer */

package org.epics.pvExampleTest.exampleClient;

import org.epics.pvdata.pv.*;
import org.epics.pvaClient.*;


public class ExamplePvaClientGet
{


    static void exampleDouble(PvaClient pva)
    {
        System.out.println("example double scalar");
        double value;
        try {
            System.out.println("short way");
            value =  pva.channel("PVRdouble").get().getData().getDouble();
            System.out.println("as double " + value);
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
        System.out.println("long way");
        PvaClientChannel pvaChannel = pva.createChannel("PVRdouble");
        pvaChannel.issueConnect();
        Status status = pvaChannel.waitConnect(2.0);
        if(!status.isOK()) {System.out.println(" connect failed"); return;}
        PvaClientGet pvaGet = pvaChannel.createGet();
        pvaGet.issueConnect();
        status = pvaGet.waitConnect();
        if(!status.isOK()) {System.out.println(" createGet failed"); return;}
        PvaClientGetData pvaData = pvaGet.getData();
        value = pvaData.getDouble();
        System.out.println("as double " + value);
    }

    static void exampleDoubleArray(PvaClient pva)
    {
        System.out.println("example double array");
        double[] value;
        try {
            System.out.println("short way");
            value =  pva.channel("PVRdoubleArray").get().getData().getDoubleArray();
            System.out.print("as doubleArray");
            for(int i=0; i<value.length; ++i) {
                System.out.print(" ");
                System.out.print(value[i]);
            }
            System.out.println();
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
        try {
            System.out.println("long way");
            PvaClientChannel pvaChannel = pva.createChannel("PVRdoubleArray");
            pvaChannel.connect(2.0);
            PvaClientGet pvaGet = pvaChannel.createGet();
            PvaClientGetData pvaData = pvaGet.getData();
            value = pvaData.getDoubleArray();
            System.out.print("as doubleArray");
            for(int i=0; i<value.length; ++i) {
                System.out.print(" ");
                System.out.print(value[i]);
            }
            System.out.println();
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
    }

    static void examplePowerSupply(PvaClient pva)
    {
        System.out.println("example powerSupply");
        PVStructure pvStructure;
        try {
            System.out.println("short way");
            pvStructure =  pva.channel("PVRpowerSupply").
            get("field()").getData().getPVStructure();
            System.out.println(pvStructure);
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }

    }

    static void exampleCADouble(PvaClient pva)
    {
        System.out.println("example double scalar");
        double value;
        try {
            System.out.println("short way");
            value =  pva.channel("DBRdouble00","ca",5.0).get().getData().getDouble();
            System.out.println("as double " + value);
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
        System.out.println("long way");
        PvaClientChannel pvaChannel = pva.createChannel("DBRdouble00","ca");
        pvaChannel.issueConnect();
        Status status = pvaChannel.waitConnect(2.0);
        if(!status.isOK()) {System.out.println(" connect failed"); return;}
        PvaClientGet pvaGet = pvaChannel.createGet();
        pvaGet.issueConnect();
        status = pvaGet.waitConnect();
        if(!status.isOK()) {System.out.println(" createGet failed"); return;}
        PvaClientGetData pvaData = pvaGet.getData();
        value = pvaData.getDouble();
        System.out.println("as double " + value);
    }

    static void exampleCADoubleArray(PvaClient pva)
    {
        System.out.println("example double array");
        double[] value;
        try {
            System.out.println("short way");
            value =  pva.channel("DBRdoubleArray","ca",5.0).get().getData().getDoubleArray();
            System.out.print("as doubleArray");
            for(int i=0; i<value.length; ++i) {
                System.out.print(" ");
                System.out.print(value[i]);
            }
            System.out.println();
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
        try {
            System.out.println("long way");
            PvaClientChannel pvaChannel = pva.createChannel("DBRdoubleArray","ca");
            pvaChannel.connect(2.0);
            PvaClientGet pvaGet = pvaChannel.createGet();
            PvaClientGetData pvaData = pvaGet.getData();
            value = pvaData.getDoubleArray();
            System.out.print("as doubleArray");
            for(int i=0; i<value.length; ++i) {
                System.out.print(" ");
                System.out.print(value[i]);
            }
            System.out.println();
        } catch (RuntimeException e) {
            System.out.println("exception " + e.getMessage());
        }
    }

    public static void main( String[] args )
    {
        PvaClient pva= PvaClient.get();
        exampleDouble(pva);
        exampleDoubleArray(pva);
        examplePowerSupply(pva);
        exampleCADouble(pva);
        exampleCADoubleArray(pva);
        System.out.println("cacheSize " + pva.cacheSize());
        System.out.println(pva.showCache());
        System.out.println("done");
    }

}
