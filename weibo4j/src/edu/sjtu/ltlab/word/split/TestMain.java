package edu.sjtu.ltlab.word.split;
import ICTCLAS.I3S.AC.ICTCLAS50;
import java.util.*;
import java.io.*;


class TestMain
{   //������
	public static void main(String[] args)
	{
		try
		{
			//�ַ�ִ�          
//			String sInput = "�����������뿪�������سǣ�Ԥ���������������ͻص����������Ͼ��ǽ�����������¶�̬";
//			testICTCLAS_ParagraphProcess(sInput);//ͬtestimportuserdict��testSetPOSmap
			//�ı��ļ��ִ�
//			testICTCLAS_FileProcess();
			testMemory();
			

		}
		catch (Exception ex)
		{
		}
	}

	public static void testMemory(){
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			//��ʼ��
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}


			//���ô��Ա�ע��(0 �����������ע����1 ������һ����ע����2 ���������ע����3 ����һ����ע��)
			testICTCLAS50.ICTCLAS_SetPOSmap(2);
			String sInput = "这是一个utf-8的句子";

			for(int i = 0; i < 10000000; i++){
				//�����û��ʵ�ǰ�ִ�
				byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("UTF-8"), 0, 1);//�ִʴ���
//				System.out.println(nativeBytes.length);
				String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
				System.out.println("δ�����û��ʵ�ķִʽ�� " + nativeStr + "    number:" + i);//��ӡ���
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}


	public static void testICTCLAS_ParagraphProcess(String sInput)
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			//��ʼ��
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}


			//���ô��Ա�ע��(0 �����������ע����1 ������һ����ע����2 ���������ע����3 ����һ����ע��)
			testICTCLAS50.ICTCLAS_SetPOSmap(2);


			//�����û��ʵ�ǰ�ִ�
			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 1);//�ִʴ���
			System.out.println(nativeBytes.length);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("δ�����û��ʵ�ķִʽ�� " + nativeStr);//��ӡ���


			//�����û��ֵ�
			int nCount = 0;
			String usrdir = "userdict.txt"; //�û��ֵ�·��
			byte[] usrdirb = usrdir.getBytes();//��stringת��Ϊbyte����
			//�����û��ֵ�,���ص����û���������һ������Ϊ�û��ֵ�·�����ڶ�������Ϊ�û��ֵ�ı�������
			nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("�����û��ʸ���" + nCount);
			nCount = 0;


			//�����û��ֵ���ٷִ�
			byte nativeBytes1[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 2, 0);
			System.out.println(nativeBytes1.length);
			String nativeStr1 = new String(nativeBytes1, 0, nativeBytes1.length, "GB2312");
			System.out.println("�����û��ʵ��ķִʽ�� " + nativeStr1);
			//�����û��ֵ�
			testICTCLAS50.ICTCLAS_SaveTheUsrDic();
			//�ͷŷִ������Դ
			testICTCLAS50.ICTCLAS_Exit();
		}
		catch (Exception ex)
		{
		}

	}



	public static void testICTCLAS_FileProcess()
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			//�ִ�������·��
			String argu = ".";
			//��ʼ��
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}

			//�����ļ���
			String Inputfilename = "test.txt";
			byte[] Inputfilenameb = Inputfilename.getBytes();//���ļ���string����תΪbyte����

			//�ִʴ��������ļ���
			String Outputfilename = "test_result.txt";
			byte[] Outputfilenameb = Outputfilename.getBytes();//���ļ���string����תΪbyte����

			//�ļ��ִ�(��һ������Ϊ�����ļ�����,�ڶ�������Ϊ�ļ���������,���������Ϊ�Ƿ��Ǵ��Լ�1 yes,0 no,���ĸ�����Ϊ����ļ���)
			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb);

			int nCount = 0;
			String usrdir = "userdict.txt"; //�û��ֵ�·��
			byte[] usrdirb = usrdir.getBytes();//��stringת��Ϊbyte����
			//��һ������Ϊ�û��ֵ�·�����ڶ�������Ϊ�û��ֵ�ı�������(0:type unknown;1:ASCII��;2:GB2312,GBK,GB10380;3:UTF-8;4:BIG5)
			nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 0);//�����û��ֵ�,���ص����û��������
			System.out.println("�����û��ʸ���" + nCount);
			nCount = 0;

			String Outputfilename1 = "testing_result.txt";
			byte[] Outputfilenameb1 = Outputfilename1.getBytes();//���ļ���string����תΪbyte����

			//�ļ��ִ�(��һ������Ϊ�����ļ�����,�ڶ�������Ϊ�ļ���������,���������Ϊ�Ƿ��Ǵ��Լ�1 yes,0 no,���ĸ�����Ϊ����ļ���)
			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb1);





		}
		catch (Exception ex)
		{
		}

	}

}


	
	

	