package de.rType.view;

import global.Score;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;

import de.rType.main.Enviroment;
import de.rType.model.Pair;
import de.rType.repository.HighScores;
import de.rType.resources.GameFonts;

/**
 * 
 * @author Jo
 * 
 */
public abstract class HighScorePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private enum PanelAction {
		READ_PLAYERNAME, SHOW_HIGHSCORES
	}

	private static final String HEADER_TEXT = "Highscores";
	private static final String BACK_TEXT = "Zurueck";
	private static final String GET_PLAYERNAME_TEXT = "Spielername eingeben: ";

	private Score currentScore;
	private String currentPlayername;
	private PanelAction action = PanelAction.SHOW_HIGHSCORES;

	public HighScorePanel() {
		setDoubleBuffered(true);
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					if (action.equals(PanelAction.SHOW_HIGHSCORES)) {
						onComplete();
					} else if (!currentPlayername.trim().isEmpty()) {
						int score = currentScore.getScore();
						currentScore = new Score(currentPlayername, score);
						HighScores.getInstance().performGameResult(currentScore);
						showHighscores(currentScore);
					}
				} else if (keyCode == KeyEvent.VK_BACK_SPACE && currentPlayername.length() > 0) {
					currentPlayername = currentPlayername.substring(0, currentPlayername.length() - 1);
				} else if (action.equals(PanelAction.READ_PLAYERNAME)) {
					currentPlayername += KeyEvent.getKeyText(keyCode);
				}
				repaint();
			};
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		setBackground(Color.BLACK);

		Pair<Integer, Integer> res = Enviroment.getEnviroment().getResolution();
		FontMetrics metr = this.getFontMetrics(GameFonts.BIG);
		g.setFont(GameFonts.BIG);
		int y = (res.getValueTwo() / (14));
		g.setColor(GameFonts.SPECIAL_COLOR);
		g.drawString(HEADER_TEXT, (res.getValueOne() - metr.stringWidth(HEADER_TEXT)) / 2, y);
		metr = this.getFontMetrics(GameFonts.SMALL);
		g.setFont(GameFonts.SMALL);
		if (action.equals(PanelAction.SHOW_HIGHSCORES)) {
			int idx = 3;
			int pos = 1;
			List<Score> scores = HighScores.getInstance().getHighScores();
			for (Score s : scores) {
				if (s != null && s.equals(currentScore)) {
					g.setColor(GameFonts.SPECIAL_COLOR);
				} else {
					g.setColor(GameFonts.DEFAULT_COLOR);
				}
				String entry = Integer.toString(pos) +" : "+  s.getPlayerName() + " - " + s.getScore();
				y = (res.getValueTwo() / (16)) * (idx + 1);
				int x = res.getValueOne()/2-50;
				g.drawString(entry, x, y);
				pos++;
				idx++;
			}
			g.setColor(GameFonts.SELECTED_COLOR);
			metr = this.getFontMetrics(GameFonts.MEDIUM);
			g.setFont(GameFonts.MEDIUM);
			y = (res.getValueTwo() / (16)) * (idx + 4);
			g.drawString(BACK_TEXT, (res.getValueOne() - metr.stringWidth(BACK_TEXT)) / 2, y);
		} else {
			g.setColor(GameFonts.DEFAULT_COLOR);
			metr = this.getFontMetrics(GameFonts.MEDIUM);
			g.setFont(GameFonts.MEDIUM);
			y = (res.getValueTwo() / (16) * 3);
			g.drawString(GET_PLAYERNAME_TEXT, (res.getValueOne() - metr.stringWidth(GET_PLAYERNAME_TEXT)) / 2, y);
			y = (res.getValueTwo() / (16) * 5);
			g.setColor(GameFonts.SPECIAL_COLOR);
			g.drawString(currentPlayername + "_", (res.getValueOne() - metr.stringWidth(currentPlayername + "_")) / 2,
					y);
		}
	}

	public void readPlayerName(int score) {
		action = PanelAction.READ_PLAYERNAME;
		currentScore = new Score(null, score);
		currentPlayername = "";
		repaint();
	}

	public void showHighscores() {
		showHighscores(null);
	}

	private void showHighscores(Score currentScore) {
		action = PanelAction.SHOW_HIGHSCORES;
		this.currentScore = currentScore;
	}

	public abstract void onComplete();
}
