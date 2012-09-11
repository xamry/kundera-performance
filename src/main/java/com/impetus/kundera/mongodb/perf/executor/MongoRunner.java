/**
 * 
 */
package com.impetus.kundera.mongodb.perf.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * The Class RuntimeProcessExmp.
 * 
 * @author Kuldeep.mishra
 * @version 1.0
 */
public class MongoRunner
{
    static Runtime runtime = Runtime.getRuntime();

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException 
     */
    public static void main(String[] argss) throws IOException, InterruptedException
    {
        int i;
        String b[] = { "1", "1000", "4000"/*, "40000", "100000", "1000000"*/};
        String c[] = { "1", "10", "100", "1000"/*, "10000", "40000", "50000", "100000"*/};
        String cb[] = { "10"/*, "100", "1000"*/};
        
        String clientArr[] = {"kundera","native", "springData", "morphia"};
        String runType[] = {"b","c","cb"};
		startMongoServer();
        for(String type : runType)
 {
			for (String client : clientArr) {

				if (type.equalsIgnoreCase("b")) {
					for (i = 0; i < b.length; i++) {

						try {
							startMongoServer();
							KunderaPerformanceRunner.main(new String[] {
									new String(b[i]), client, type });
							 dropDatabase("kundera");
//							stopMongoServer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (type.equalsIgnoreCase("c")) {
					for (i = 0; i < c.length; i++) {

						try {
//							startMongoServer();
							KunderaPerformanceRunner.main(new String[] { "1",
									client, type, new String(c[i]) });
							 dropDatabase("kundera");
							// dropDatabase(args[0].toLowerCase());
//							stopMongoServer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (type.equalsIgnoreCase("cb")) {

					for (i = 0; i < cb.length; i++) {

						try {
//							startMongoServer();
							KunderaPerformanceRunner.main(new String[] {
									"1000", client, type, new String(cb[i]) });
							 dropDatabase("kundera");
						// dropDatabase(args[0].toLowerCase());
//							stopMongoServer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("please give valid options ie b/c/cb");
				}

			}
		}

        onGenerateDelta(KunderaPerformanceRunner.profiler);
    }

    /**
     * Drop database.
     * 
     * @param dbname
     *            the dbname
     * @throws UnknownHostException
     *             the unknown host exception
     * @throws InterruptedException
     */
    private static void dropDatabase(String dbname) throws UnknownHostException, InterruptedException
    {
        Mongo m = new Mongo();
        DB db = m.getDB(dbname);
        db.dropDatabase();
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Start mongo server.
     * 
     * @param runtime
     *            the runtime
     * @return the process
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     */
    private static void startMongoServer() throws IOException, InterruptedException
    {
        runtime.exec("/home/impadmin/vivek/mongodb-linux-i686-1.8.4/bin/mongod").waitFor();
//        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Stop mongo server.
     * 
     * @param runtime
     *            the runtime
     * @param br
     *            the br
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     */
    private static void stopMongoServer() throws IOException, InterruptedException
    {
        Process process = runtime.exec("ps -u impadmin");
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null)
        {
            if (line.contains("mongod"))
            {
                int idx;
                if (line.indexOf('?') > 0)
                {
                    idx = line.indexOf('?');
                }
                else
                {
                    idx = line.indexOf("pts");
                }

                runtime.exec("kill -9 " + line.substring(0, idx - 1)).waitFor();
                System.out.println("Killed process " + line.substring(0, idx - 1));
                deleteLockFile();
//                TimeUnit.SECONDS.sleep(5);
            }
        }
    }

    /**
     * Delete lock file.
     * 
     * @throws IOException
     */
    private static void deleteLockFile() throws IOException
    {
        File mongodbLockFile = new File("/data/db/mongod.lock");
        mongodbLockFile.delete();
        runtime.exec("rm -rf /data/db/*");
        System.out.println("Deleted File" + mongodbLockFile);
    }

    
    private static void onGenerateDelta(Map<String, Long> profiledData) throws FileNotFoundException, IOException
    {
//  	  String b[] = { "1", "1000", "4000", "40000", "100000", "1000000" };
//      String c[] = { "1", "10", "100", "1000", "10000", "40000", "50000", "100000" };
//      String cb[] = { "10", "100", "1000" };

    	String b[] = { "1", "1000", "4000"/*, "40000", "100000", "1000000"*/};
        String c[] = { "1", "10", "100", "1000"/*, "10000", "40000", "50000", "100000"*/};
        String cb[] = { "10"/*, "100", "1000"*/};
        
    	String fileName = "performance_mongo.xls";
       HSSFWorkbook workBook = new HSSFWorkbook();
       workBook = generateDelta(b, profiledData, workBook, "Bulk", "b");
       workBook = generateDelta(c, profiledData, workBook, "Concurrent", "c");
       workBook = generateDelta(cb, profiledData, workBook, "Concurrent-Bulk", "cb");
      
		 FileOutputStream fos = null;
	        try {
	            fos = new FileOutputStream(new File(fileName));
	            workBook.write(fos);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fos != null) {
	                try {
	                    fos.flush();
	                    fos.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

    }

    private static  HSSFWorkbook generateDelta(String[] type, Map<String, Long> profiledData, HSSFWorkbook workBook, String sheetName, String keyType) throws FileNotFoundException, IOException
    {
        String clientArr[] = {"kundera","native", "springData", "morphia"};
        HSSFSheet sheet = initSheet(workBook,sheetName);

        int nextRowCount = 2;
		for (String t : type) 
		{
	        int intnoOfthreads = 1;
	        String noOfRecord = t;
	        // set on no of record and threads.
	        
	        if(keyType.equals("c"))
	        {
	        	intnoOfthreads = Integer.parseInt(t);
	        	noOfRecord = 1+"";
	        } else if(keyType.equals("cb"))
	        {
	        	intnoOfthreads = Integer.parseInt(t);
	        	noOfRecord = 1000 + "";
	        }
	        
	        
	        String keySeperator = ":" + keyType; 
			
	        HSSFRow dataRow = sheet.createRow(nextRowCount);
			HSSFCell noOfThreadCell = dataRow.createCell(0);
			noOfThreadCell.setCellValue(intnoOfthreads);
			HSSFCell noOfRecordsCell = dataRow.createCell(1);
			noOfRecordsCell.setCellValue(noOfRecord);
			
			
			int clientCnt=2;
			
			// iterate for earch client.
			double[] cellValArr = new double[4];
			int cnt=0;
			for (String client : clientArr)
			{
				String key = client +keySeperator+ ":" + noOfRecord + ":" + intnoOfthreads;
				System.out.println(key);
				Long timeTaken = profiledData.get(key);
				System.out.println(timeTaken);
				HSSFCell clientCell = dataRow.createCell(clientCnt);
				clientCell.setCellValue(timeTaken);
				clientCnt++;
				cellValArr[cnt++] = timeTaken;
			}

			// populate delta.
			HSSFCell kundera_native = dataRow.createCell(clientCnt++);
			kundera_native.setCellValue(((cellValArr[0]-cellValArr[1])/cellValArr[1]) * 100);
			
			HSSFCell kundera_springdata = dataRow.createCell(clientCnt++);
			kundera_springdata.setCellValue(((cellValArr[0]-cellValArr[2])/cellValArr[2]) * 100);

			HSSFCell kundera_morphia = dataRow.createCell(clientCnt);
			kundera_morphia.setCellValue(((cellValArr[0]-cellValArr[3])/cellValArr[3]) * 100);

			nextRowCount++;
		}
		
		return workBook;
		
	}

	private static HSSFSheet initSheet(HSSFWorkbook workbook, String sheetName) {
		HSSFSheet sheet = workbook.createSheet(sheetName);
        
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellValue("Performance Analysis:");

        HSSFRow row1 = sheet.createRow(1);
        HSSFCell cell1 = row1.createCell(0);
        cell1.setCellValue("NoOfThreads");
        HSSFCell cell2 = row1.createCell(1);
        cell2.setCellValue("NoOfRecords");
        HSSFCell cell3 = row1.createCell(2);
        cell3.setCellValue("kundera");
        HSSFCell cell4 = row1.createCell(3);

        cell4.setCellValue("native");
        
        HSSFCell cell5 = row1.createCell(4);
        cell5.setCellValue("springData");
        
        HSSFCell cell6 = row1.createCell(5);
        cell6.setCellValue("morphia");
        
        HSSFCell cell7 = row1.createCell(6);
        cell7.setCellValue("kundera-native(%)");

        HSSFCell cell8 = row1.createCell(7);
        cell8.setCellValue("kundera-springdata(%)");

        HSSFCell cell9 = row1.createCell(8);
        cell9.setCellValue("kundera-morphia(%)");
		return sheet;
	}
}
