package com.cppteam.common.util;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadCallback;

/**
 * FastDfs 图片上传客户端简单封装
 * @author happykuan
 */
public class FastDFSClient {

	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private StorageClient1 storageClient = null;

	public FastDFSClient(String conf) throws Exception {

		if (conf.contains("classpath:")) {
			String url = this.getClass().getResource("/").getPath();
			url = url.substring(1);
			conf = conf.replace("classpath:", url);
		}
		ClientGlobal.init(conf);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	// 上传文件
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		return storageClient.upload_file1(fileName, extName, metas);
	}

	public String uploadFile(String fileName, String extName) throws Exception {
		return storageClient.upload_file1(fileName, extName, null);
	}

	public String uploadFile(String fileName) throws Exception {
		return storageClient.upload_file1(fileName, null, null);
	}

	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
		return storageClient.upload_file1(fileContent, extName, metas);
	}

	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return storageClient.upload_file1(fileContent, extName, null);
	}

	public String uploadFile(byte[] fileContent) throws Exception {
		return storageClient.upload_file1(fileContent, null, null);
	}
	// 删除文件
	public int deleteFile(String groupName, String remoteFileName) throws IOException, MyException {
		return storageClient.delete_file(groupName, remoteFileName);
	}
	public int deleteFile(String fileId) throws IOException, MyException {
		return storageClient.delete_file1(fileId);
	}

	public int modify_file(String group_name, String appender_filename, long file_offset, String local_filename)
			throws IOException, MyException {
		return storageClient.modify_file(group_name, appender_filename, file_offset, local_filename);
	}

	public int modify_file(String group_name, String appender_filename, long file_offset, byte[] file_buff)
			throws IOException, MyException {
		return storageClient.modify_file(group_name, appender_filename, file_offset, file_buff);
	}

	public int modify_file(String group_name, String appender_filename, long file_offset, byte[] file_buff,
			int buffer_offset, int buffer_length) throws IOException, MyException {
		return storageClient.modify_file(group_name, appender_filename, file_offset, file_buff, buffer_offset,
				buffer_length);
	}

	public int modify_file(String group_name, String appender_filename, long file_offset, long modify_size,
			UploadCallback callback) throws IOException, MyException {
		return storageClient.modify_file(group_name, appender_filename, file_offset, modify_size, callback);
	}
}