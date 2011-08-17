package hum.client.widget;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * HourMinutePicker.java
 * 
 * @author ochoa
 * 
 */
public class HourMinutePicker extends Composite {

	public enum PickerFormat {
		_12_HOUR, _24_HOUR
	}

	private PickerFormat format;
	private int startHour;
	private int endHour;
	private int timeSlices;

	/*
	 * User-selected time attributes
	 */
	private int selectedHour = -1;
	private int selectedMinute = -1;
	private String selectedSuffix = "";

	/*
	 * Working copy of the time attributes
	 */
	private int workingHour = -1;
	private int workingMinute = -1;
	private String workingSuffix = "";

	private FlowPanel pnlMain = new FlowPanel();
	private InlineHTML lblInput = new InlineHTML();
	private FocusPanel pnlFocus = new FocusPanel();
	private FlowPanel pnlPopup = new FlowPanel();

	private FlowPanel pnlSuffix = new FlowPanel();
	private FlowPanel pnlHoursAM = new FlowPanel();
	private FlowPanel pnlHoursPM = new FlowPanel();
	private FlowPanel pnlMinutes = new FlowPanel();

	private ArrayList<InlineLabel> hourLabels = new ArrayList<InlineLabel>();
	private ArrayList<InlineLabel> minuteLabels = new ArrayList<InlineLabel>();
	private InlineLabel amSuffixLabel;
	private InlineLabel pmSuffixLabel;
	private InlineLabel clearLabel;
	protected boolean closePopupOnBlur = false;

	private MouseOverHandler suffixSelectedHandler = new MouseOverHandler() {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			closePopupOnBlur = false;
			Widget source = (Widget) event.getSource();

			String suffix = source.getElement().getPropertyString("suffix");
			int hour = workingHour;
			int minute = workingMinute == -1 ? 0 : workingMinute;

			if (workingHour == -1) {
				hour = startHour;
			}
			if ("AM".equals(suffix)) {
				hour = hour % 12 >= startHour ? hour % 12 : startHour;
			} else if ("PM".equals(suffix)) {
				hour = (hour % 12) + 12 < endHour ? (hour % 12) + 12 : 12;
			} else {
				hour = -1;
				minute = -1;
			}

			refreshWidget(suffix, hour, minute);
		}
	};

	private MouseOverHandler hourSelectedHandler = new MouseOverHandler() {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			closePopupOnBlur = false;
			Widget source = (Widget) event.getSource();
			refreshWidget(
					(workingSuffix == null || "".equals(workingSuffix)) ? "AM"
							: workingSuffix, source.getElement()
							.getPropertyInt("hour"), workingMinute == -1 ? 0
							: workingMinute);
		}
	};

	private MouseOverHandler minuteSelectedHandler = new MouseOverHandler() {
		@Override
		public void onMouseOver(MouseOverEvent event) {
			closePopupOnBlur = false;
			Widget source = (Widget) event.getSource();
			refreshWidget(
					(workingSuffix == null || "".equals(workingSuffix)) ? "AM"
							: workingSuffix, workingHour == -1 ? startHour
							: workingHour,
					source.getElement().getPropertyInt("minute"));
		}
	};

	private ClickHandler closePopupHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			selectedHour = workingHour;
			selectedMinute = workingMinute;
			selectedSuffix = workingSuffix;
			pnlPopup.setVisible(false);
		}
	};

	private MouseOutHandler isOkToCloseHandler = new MouseOutHandler() {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			closePopupOnBlur = true;
		}
	};

	public HourMinutePicker(PickerFormat format, int startHour, int endHour,
                            int timeSlices) {
		this.startHour = Math.abs(startHour % 24);
		this.endHour = Math.abs(endHour + 1 % 24);
		this.timeSlices = timeSlices;
		this.format = format;
		createBody();
		initWidget(pnlMain);
	}

	public HourMinutePicker(PickerFormat format) {
		this(format, 0, 23, 4);
	}

	/**
	 * Creates the widget panels
	 */
	private void createBody() {

		pnlMain.addStyleName("timepickr-main");
		pnlMain.add(pnlFocus);

		pnlFocus.setStyleName("timepickr-display");
		pnlFocus.add(lblInput);

		pnlFocus.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				pnlPopup.setVisible(true);
				refreshWidget(selectedSuffix, selectedHour, selectedMinute);
				closePopupOnBlur = true;
			}
		});

		pnlFocus.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (closePopupOnBlur) {
					refreshWidget(selectedSuffix, selectedHour, selectedMinute);
					pnlPopup.setVisible(false);
				}
			}
		});

		pnlHoursAM.addStyleName("timepickr-row");
		pnlHoursPM.addStyleName("timepickr-row");
		pnlHoursPM.setVisible(false);
		pnlMinutes.addStyleName("timepickr-row");
		pnlSuffix.addStyleName("timepickr-row");

		for (int i = 0; i < timeSlices; i++) {
			int min = i * (60 / timeSlices);
			InlineLabel minuteLabel = createInlineLabel(
					NumberFormat.getFormat("00").format(min),
					"timepickr-button");
			minuteLabel.addMouseOverHandler(minuteSelectedHandler);
			minuteLabel.addClickHandler(closePopupHandler);
			minuteLabel.addMouseOutHandler(isOkToCloseHandler);
			minuteLabel.getElement().setPropertyInt("minute", i);
			pnlMinutes.add(minuteLabel);
			minuteLabels.add(minuteLabel);
		}

		// AM hours
		for (int i = (startHour < 12 ? startHour : 12); i < (endHour < 12 ? endHour
				: 12); i++) {
			InlineLabel hourLabelAM = createInlineLabel(
					NumberFormat.getFormat("00").format(i), "timepickr-button");
			hourLabelAM.addMouseOverHandler(hourSelectedHandler);
			hourLabelAM.addClickHandler(closePopupHandler);
			hourLabelAM.addMouseOutHandler(isOkToCloseHandler);
			hourLabelAM.getElement().setPropertyInt("hour", i);
			pnlHoursAM.add(hourLabelAM);
			hourLabels.add(hourLabelAM);
		}

		// PM hours
		for (int i = (startHour < 12 ? 12 : startHour); i < (endHour < 12 ? 12
				: endHour); i++) {
			InlineLabel hourLabelPM = createInlineLabel(
					NumberFormat.getFormat("00").format(
							PickerFormat._24_HOUR.equals(format) ? i
									: (i > 12 ? i % 12 : i)),
					"timepickr-button");
			hourLabelPM.addMouseOverHandler(hourSelectedHandler);
			hourLabelPM.addClickHandler(closePopupHandler);
			hourLabelPM.addMouseOutHandler(isOkToCloseHandler);
			hourLabelPM.getElement().setPropertyInt("hour", i);
			pnlHoursPM.add(hourLabelPM);
			hourLabels.add(hourLabelPM);
		}

		if (startHour < 12 && endHour > 12) {
			if (startHour < 12) {
				amSuffixLabel = createInlineLabel("AM", "timepickr-button");
				amSuffixLabel.getElement().setPropertyString("suffix", "AM");
				amSuffixLabel.addMouseOverHandler(suffixSelectedHandler);
				amSuffixLabel.addClickHandler(closePopupHandler);
				amSuffixLabel.addMouseOutHandler(isOkToCloseHandler);
			} else {
				pnlHoursPM.setVisible(true);
				pnlHoursAM.setVisible(false);
			}

			if (endHour > 12) {
				pmSuffixLabel = createInlineLabel("PM", "timepickr-button");
				pmSuffixLabel.getElement().setPropertyString("suffix", "PM");
				pmSuffixLabel.addMouseOverHandler(suffixSelectedHandler);
				pmSuffixLabel.addClickHandler(closePopupHandler);
				pmSuffixLabel.addMouseOutHandler(isOkToCloseHandler);
			}

			pnlSuffix.add(amSuffixLabel);
			pnlSuffix.add(pmSuffixLabel);
		}
		clearLabel = createInlineLabel("clear", "timepickr-button");
		clearLabel.getElement().setPropertyString("suffix", "");
		clearLabel.addMouseOverHandler(suffixSelectedHandler);
		clearLabel.addClickHandler(closePopupHandler);
		clearLabel.addMouseOutHandler(isOkToCloseHandler);

		pnlSuffix.add(clearLabel);
		pnlPopup.add(pnlSuffix);

		pnlPopup.add(pnlHoursAM);
		pnlPopup.add(pnlHoursPM);
		pnlPopup.add(pnlMinutes);

		pnlPopup.addStyleName("timepickr-popup");
		pnlPopup.setVisible(false);

		pnlMain.add(pnlPopup);

	}

	/**
	 * Refreshes the input text
	 */
	private void refreshInputText() {
		String timeText = "";

		if (workingHour > -1) {
			if (format.equals(PickerFormat._12_HOUR)) {
				timeText += NumberFormat.getFormat("00").format(
						workingHour % 12 == 0 ? 12 : workingHour % 12)
						+ ":";
				timeText += NumberFormat.getFormat("00").format(
						workingMinute * (60 / timeSlices));
				timeText += "&nbsp;";
				timeText += workingSuffix;
			} else {
				timeText += NumberFormat.getFormat("00").format(workingHour)
						+ ":";
				timeText += NumberFormat.getFormat("00").format(
						workingMinute * (60 / timeSlices));
			}
		}

		lblInput.setHTML(timeText);
	}

	/**
	 * Re-paints the widget using the provided suffix, hour and minute.
	 */
	private void refreshWidget(String suffix, int hour, int minute) {

		if (!this.workingSuffix.equals(suffix)) {
			this.workingSuffix = suffix;
			if ("AM".equals(this.workingSuffix)) {
				pnlHoursAM.setVisible(true);
				pnlHoursPM.setVisible(false);

				clearLabel.removeStyleName("state-hover");
				pmSuffixLabel.removeStyleName("state-hover");
				amSuffixLabel.addStyleName("state-hover");

			} else if ("PM".equals(this.workingSuffix)) {
				pnlHoursAM.setVisible(false);
				pnlHoursPM.setVisible(true);

				clearLabel.removeStyleName("state-hover");
				amSuffixLabel.removeStyleName("state-hover");
				pmSuffixLabel.addStyleName("state-hover");

			} else {
				pnlHoursAM.setVisible(true);
				pnlHoursPM.setVisible(false);

				pmSuffixLabel.removeStyleName("state-hover");
				amSuffixLabel.removeStyleName("state-hover");
				clearLabel.addStyleName("state-hover");
			}
		}
/*
		DOM.setStyleAttribute(
				pnlHoursAM.getElement(),
				"left",
				2 - (pnlHoursAM.getOffsetWidth() / 2)
						- (pnlHoursAM.getOffsetWidth() % 2)
						+ (amSuffixLabel.getOffsetWidth() / 2)
						+ (amSuffixLabel.getOffsetWidth() % 2) + "px");

		DOM.setStyleAttribute(
				pnlHoursPM.getElement(),
				"left",
				pmSuffixLabel.getElement().getOffsetLeft()
						+ (pmSuffixLabel.getOffsetWidth() / 2)
						- (pnlHoursPM.getOffsetWidth() / 2) + "px");
*/

		if (this.workingHour != hour) {
			this.workingHour = hour;

			for (int i = 0; i < hourLabels.size(); i++) {
				if ((this.workingHour - startHour) != i) {
					hourLabels.get(i).removeStyleName("state-hover");
				} else {
					hourLabels.get(i).addStyleName("state-hover");
				}
			}

		}

		if (workingHour != -1) {
			InlineLabel hourLabel = hourLabels
					.get(this.workingHour - startHour);
/*
			DOM.setStyleAttribute(
					pnlMinutes.getElement(),
					"left",
					hourLabel.getAbsoluteLeft() - lblInput.getAbsoluteLeft()
							- (pnlMinutes.getOffsetWidth() / 2)
							+ (hourLabel.getOffsetWidth() / 2) + "px");
*/
		} else {
/*
			DOM.setStyleAttribute(
					pnlMinutes.getElement(),
					"left",
					2 - (pnlMinutes.getOffsetWidth() / 2)
							- (pnlMinutes.getOffsetWidth() % 2)
							+ (amSuffixLabel.getOffsetWidth() / 2)
							+ (amSuffixLabel.getOffsetWidth() % 2) + "px");
*/
		}
		if (this.workingMinute != minute) {
			this.workingMinute = minute;
			for (int i = 0; i < minuteLabels.size(); i++) {
				if (this.workingMinute != i) {
					minuteLabels.get(i).removeStyleName("state-hover");
				} else {
					minuteLabels.get(i).addStyleName("state-hover");
				}
			}
		}

		refreshInputText();
	}

	/**
	 * Create an inline label with the list of styles
	 */
	private InlineLabel createInlineLabel(String txt, String... styles) {
		InlineLabel lbl = new InlineLabel(txt);
		for (String style : styles) {
			lbl.addStyleName(style);
		}
		return lbl;
	}

	/**
	 * Get the time in minutes since midnight
	 */
	public Integer getMinutes() {
		if (selectedHour > -1) {
			return selectedHour * 60 + (selectedMinute * (60 / timeSlices));
		} else {
			return null;
		}
	}

	/**
	 * Returns the selected hour
	 */
	public Integer getHour() {
		return selectedHour;
	}

	/**
	 * Returns the selected minute
	 */
	public Integer getMinute() {
		return selectedMinute * (60 / timeSlices);
	}

	/**
	 * Sets the widget initial time considering the constraints it was build
	 * upon.
	 * 
	 * @param suffix
	 *            : is ignored when the format is 24hrs
	 * @param hour
	 *            :
	 * @param minute
	 */
	public void setTime(String suffix, int hour, int minute) {
		this.selectedSuffix = suffix == null || !suffix.equals("AM") || !suffix.equals("PM") ? "AM"
				: suffix;
		this.selectedHour = hour;
		this.selectedMinute = minute / (60 / timeSlices);
		refreshWidget(selectedSuffix, selectedHour, selectedMinute);
	}

	/**
	 * Reset the hour/minute
	 */
	public void clear() {
		this.selectedHour = -1;
		this.selectedMinute = -1;
		this.selectedSuffix = "";
		refreshWidget(selectedSuffix, selectedHour, selectedMinute);
	}

}
