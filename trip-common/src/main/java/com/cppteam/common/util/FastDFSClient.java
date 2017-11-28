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

		String path = "";
		if (conf.contains("classpath:")) {
			// this.getClass().getResource("/");获取的是URL文本路径 调用 .getPath()获取类的绝对路径（/C://...）
			String url = this.getClass().getResource("/").getPath();

			// 在linux服务器上使用绝对路径，不需要去掉前面的"/"
			// url = url.substring(1);
			conf = conf.replace("classpath:", url);
			path = conf;
		}
		path = path.substring(path.lastIndexOf(".") + 1, path.length());
		if (path.equalsIgnoreCase("conf")) {
			ClientGlobal.init(conf);
		} else {
			ClientGlobal.initByProperties(conf);
		}

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

}