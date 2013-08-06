package suggestor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxUI;

import structures.TrieHeader;


/**
 * This acts like a text field which provides suggestions as you type.
 * 
 * @author David X. Wang
 * 
 * @param <E> The type of object which will populate the drop-down box, must have a display-friendly toString method.
 */
@SuppressWarnings("serial")
public class AutoSuggestionTextField<E> extends JComboBox{

	private TrieHeader trie;
	private SuggestionListener<E> listener;
	private String previousText = "";
	private JTextField textField;
	private Set<String> currentSuggestions = new TreeSet<String>();
	private boolean suppressEvents = false;

	public AutoSuggestionTextField(){
		super();
		//remove arrow
		setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					@Override
					public int getWidth() {
						return 0;
					}
				};
			}

			@Override
			public void setPopupVisible(JComboBox c, boolean v) {
				// keeps the popup from coming down if there's nothing in the combo box
				if (c.getItemCount() > 0) {
					super.setPopupVisible(c, v);
				} else{
					super.setPopupVisible(c, false);
				}
			}
		});

		//properties and components
		setEditable(true);
		ItemSelectionListener lstnr = new ItemSelectionListener();
		addActionListener(lstnr);
		addItemListener(lstnr);
		textField = (JTextField) getEditor().getEditorComponent();
		textField.addKeyListener(new TextListener());
	}

	public String getElement(int index){
		return (String) this.getItemAt(index);
	}

	public void setTrie(TrieHeader trie) {
		this.trie=trie;
	}
	
	public JTextField getTextField(){
		return this.textField;
	}

	public SuggestionListener<E> getSuggestionListener() {
		return listener;
	}

	public void setSuggestionListener(SuggestionListener<E> listener) {
		this.listener = listener;
	}

	private class TextListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==e.VK_UP||e.getKeyCode()==e.VK_DOWN||e.getKeyCode()==e.VK_LEFT||e.getKeyCode()==e.VK_RIGHT||e.getKeyCode()==e.VK_ENTER)return;
			String text = textField.getText();
			if(text.equals(previousText))
				return;

			previousText = text;

			suppressEvents = true;

			if(getItemCount() != 0)
				removeAllItems();

			//populate drop down
			currentSuggestions = trie.listPrefix(text);
			if(currentSuggestions!=null){
				for(String item : currentSuggestions)
					addItem(item);
			}

			setPopupVisible(false);

			if(currentSuggestions!=null)
				setPopupVisible(true);

			setSelectedItem(text);

			suppressEvents = false;
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}

	private class ItemSelectionListener implements ItemListener, ActionListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(suppressEvents)
				return;

			if(e.getStateChange() == ItemEvent.SELECTED)
				passEvent();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(suppressEvents)
				return;

			if(e.getActionCommand().equals("comboBoxEdited"))
				passEvent();
		}

		@SuppressWarnings("unchecked")
		private void passEvent(){
			if(listener == null)
				return;

			Object item = getSelectedItem();
			
			if(item instanceof String){
				listener.onEnter((String)item);
			} else{
				listener.onSuggestionSelected((E)item);
			}
		}
	}
}
