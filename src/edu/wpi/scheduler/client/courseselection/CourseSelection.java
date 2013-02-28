package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.controller.StudentScheduleEvents;
import edu.wpi.scheduler.shared.model.Course;

public class CourseSelection extends ComplexPanel implements StudentScheduleEventHandler {

	private CourseSelectionController selectionController;

	public CourseSelection(final CourseSelectionController selectionController) {
		this.selectionController = selectionController;

		this.setElement(DOM.createTable());

		this.setStyleName("courseList");

	}

	@Override
	public void add(Widget child) {
		this.add(child, this.getElement());
	}

	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad() {
		StudentSchedule schedule = selectionController.getStudentSchedule();
		schedule.addStudentScheduleHandler(this);
		
		this.clear();
		
		for( SectionProducer producer : schedule.sectionProducers ){
			addCourse(producer.getCourse());
		}		
	}

	@Override
	protected void onUnload() {
		selectionController.getStudentSchedule().removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent event) {
		if( event.event == StudentScheduleEvents.ADD){
			CourseSelectionItem item = addCourse(event.getCourse());
			
			if( event.getWidgetSourse() != null ){
				CourseAddAnimation animation = new CourseAddAnimation(item, event.getWidgetSourse());
				animation.run(1000);
			}
			
		} else if (event.event == StudentScheduleEvents.REMOVE){
			removeCourse(event.getCourse());
		}
	
	}
	
	private CourseSelectionItem addCourse( Course course ){
		CourseSelectionItem item = new CourseSelectionItem(selectionController, course);

		this.add(item);
		
		return item;
	}
	
	private void removeCourse( Course course ){
		for (Widget widget : this.getChildren()) {
			CourseSelectionItem item = (CourseSelectionItem) widget;

			if (item.getCourse().equals(course)) {
				this.remove(widget);
			}
		}		
	}

}
