package it.giara.utils;

public class ErrorHandler
{

	public static void fileNotAvailable(String fileName)
	{
		Log.log(Log.ERROR, "Siamo Spiacenti ma il file "+fileName+" non è al momento disponibile");
	//TODO
	}
	
	public static void FailServerConnect(String error)
	{
		Log.log(Log.ERROR, error);
	//TODO
	}
	
	public static void noRequestFileFromThisBot(String error)
	{
		Log.log(Log.ERROR, error);
		//TODO
	}
	
	public static void BotSendWrongFile(String file1, String file2)
	{
		Log.log(Log.ERROR, "-"+file1+"- != -"+file2+"-");
		//TODO
	}

	public static void alreadyInDownload(String filename)
	{
		Log.log(Log.ERROR, "Il file: "+filename+" è già in download");
		//TODO
	}
	
}
