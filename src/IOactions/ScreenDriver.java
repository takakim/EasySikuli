package IOactions;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.script.App;

import IOactions.SecAction.PostFind;
import IOactions.SecAction.PostWait;

/**
 * @author Carlos Leal
 *
 */
public class ScreenDriver {

	public Mouse mouse = new DesktopMouse();
	public Keyboard keyboard = new DesktopKeyboard();
	
	private PostFind postFind;
	private PostWait postWait;
	
	static int implicityWait = 20000;
	int timeResult = 0;
	double defaultSimilarity = 0.70;

	
	/**
	 * M�todo que espera, ate o tempo informado, um elemento(imagem) aparecer na tela.
	 * 
	 * @param imagePath endereco(dentro do projeto) da imagem a ser aguardada; 
	 * @param timeInSeconds tempo de espera;
	 * @param similarity grau de similaridade da imagem a ser aguardada (min 0.01, max 0.99);
	 * 
	 * @return postWait objeto com o valor informado de todos os parametros
	 */
	public PostWait waitFor(String imagePath, int timeInSeconds,double similarity) {
		ScreenRegion myDesktop = new DesktopScreenRegion();
		File image = new File(imagePath);
		Target imageTarget = new ImageTarget(image);
		imageTarget.setMinScore(similarity);
		
		timeResult = timeInSeconds * 1000;
		myDesktop.wait(imageTarget, timeResult);
		myDesktop.find(imageTarget);

		//PostWait
		postWait = new PostWait();
		postWait.setImagePath(imagePath);
		postWait.setTimeInSeconds(timeInSeconds);
		postWait.setSimilarity(similarity);
		return postWait;
	}

	/**
	 * M�todo que espera, ate o tempo informado, um elemento(imagem) aparecer na tela.
	 * 
	 * @param imagePath endereco(dentro do projeto) da imagem a ser aguardada; 
	 * @param timeInSeconds tempo de espera;
	 * 
	 * @return postWait
	 */
	public PostWait waitFor(String imagePath, int timeInSeconds) {
		postWait = waitFor(imagePath, timeInSeconds, defaultSimilarity);

		return postWait;
	}
	
	
	/**
	 * M�todo que escreve em qualquer campo de texto que estiver com o foco. 
	 * 
	 * @param text texto que ser� inserido no campo;
	 */
	public void type(String text){
		keyboard.type(text);
	}

	
	/**
	 * M�todo que procura uma imagem na tela atual e � ponto inicial para outras a��es, como:
	 * click, doubleClick, type, etc.
	 * 
	 * @param imagePath endereco(dentro do projeto) da imagem a ser procurada;
	 * @param similarity grau de similaridade da imagem a ser aguardada (min 0.01, max 0.99);
	 * 
	 * @return postFind objeto com o valor informado de todos os parametros;
	 */
	public PostFind findImage(String imagePath, double similarity) {
		ScreenRegion myDesktop = new DesktopScreenRegion();
		File image = new File(imagePath);
		Target imageTarget = new ImageTarget(image);
		imageTarget.setMinScore(similarity);

		myDesktop.wait(imageTarget, implicityWait);
		myDesktop.find(imageTarget);

		// PosFind
		postFind = new PostFind();
		postFind.setImagePath(imagePath);
		postFind.setSimilarity(similarity);
		return postFind;
	}

	/**
	 * M�todo que procura uma imagem na tela atual e � ponto inicial para outras a��es, como:
	 * click, doubleClick, type, etc.
	 * 
	 * @param imagePath endereco(dentro do projeto) da imagem a ser procurada;
	 * @return postFind objeto com o valor informado de todos os parametros;
	 */
	public PostFind findImage(String imagePath) {
		postFind = findImage(imagePath, defaultSimilarity);

		return postFind;
	}

	/**
	 * M�todo que funciona como debug, procura uma imagem na tela atual e contorna ela com um box de cor vermelha;
	 * 
	 * @param imagePath endereco(dentro do projeto) da imagem a ser procurada;
	 */
	public void findHightLight(String imagePath) {
		ScreenRegion myDesktop = new DesktopScreenRegion();
		File image = new File(imagePath);
		Target imageTarget = new ImageTarget(image);
		imageTarget.setMinScore(defaultSimilarity);

		ScreenRegion myScreen = myDesktop;
		myScreen.wait(imageTarget, implicityWait);
		myScreen = myDesktop.find(imageTarget);

		Canvas canvas = new DesktopCanvas();
		canvas.addBox(myScreen).withLineWidth(4);
		canvas.display(5);
	}

	public void assertImageExists(String expectedImagePath, double similarity,
			int timeInSeconds) {
		int timeResult;
		ScreenRegion myDesktop = new DesktopScreenRegion();
		File image = new File(expectedImagePath);
		Target imageTarget = new ImageTarget(image);
		imageTarget.setMinScore(similarity);

		ScreenRegion myScreen = myDesktop;
		timeResult = timeInSeconds * 1000;
		myScreen.wait(imageTarget, timeResult);
		myScreen = myDesktop.find(imageTarget);
		if (myScreen == null) {
			screenShot();
		}
		Assert.assertNotNull(myScreen);
	}

	public void assertImageExists(String expectedImagePath, double similarity) {
		assertImageExists(expectedImagePath, similarity, implicityWait);
	}

	public void assertImageExists(String expectedImagePath) {
		assertImageExists(expectedImagePath, defaultSimilarity, implicityWait);
	}

	private void screenShot() {
		keyboard = new DesktopKeyboard();
		keyboard.type(Key.PAUSE);
	}
	
	public void openApplication(String appAddress) {
		App.open(appAddress);
	}
	
	public void openDefaultBrowse(String browseUrl) throws URISyntaxException, IOException{
		URI url = new URI(browseUrl);
		java.awt.Desktop.getDesktop().browse(url);
	}
	
	public void openTxtFile(String appAddress) throws IOException{
		File file = new File(appAddress);
		java.awt.Desktop.getDesktop().edit(file);
	}
	
	public void pressEnter() {
		keyboard.type(Key.ENTER);
	}
}
