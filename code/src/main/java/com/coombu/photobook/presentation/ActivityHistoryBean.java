package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.coombu.photobook.model.ActivityHistory;
import com.coombu.photobook.service.IActivityHistoryService;
import com.coombu.photobook.util.Constants.ACTIVITY_TYPE;

@Named("historyBean")
@Scope(value = "view")
public class ActivityHistoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(ActivityHistoryBean.class);

	private List<ActivityHistory> allActivities;

	private List<ActivityHistory> myActivity;

	private List<ActivityHistory> groupActivity;

	private Map<String, List<ActivityHistory>> myPaginationList;

	private Map<String, List<ActivityHistory>> groupPaginationList;

	private int myStartIndex;

	private int groupStartIndex;

	private int maxFetchCount;

	private long myTotalCount;

	private long groupTotalCount;

	private String component;

	private int myCurrentPage;

	private int groupCurrentPage;

	private int myTotalPages;

	private int groupTotalPages;

	@Autowired
	private DashBean dashBean;

	public int getMyTotalPages() {
		return myTotalPages;
	}

	public void setMyTotalPages(int myTotalPages) {
		this.myTotalPages = myTotalPages;
	}

	public int getGroupTotalPages() {
		return groupTotalPages;
	}

	public void setGroupTotalPages(int groupTotalPages) {
		this.groupTotalPages = groupTotalPages;
	}

	public int getMyCurrentPage() {
		return myCurrentPage;
	}

	public void setMyCurrentPage(int myCurrentPage) {
		this.myCurrentPage = myCurrentPage;
	}

	public int getGroupCurrentPage() {
		return groupCurrentPage;
	}

	public void setGroupCurrentPage(int groupCurrentPage) {
		this.groupCurrentPage = groupCurrentPage;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	@Autowired
	private IActivityHistoryService activityService;

	public int getMyStartIndex() {
		return myStartIndex;
	}

	public void setMyStartIndex(int myStartIndex) {
		this.myStartIndex = myStartIndex;
	}

	public int getGroupStartIndex() {
		return groupStartIndex;
	}

	public void setGroupStartIndex(int groupStartIndex) {
		this.groupStartIndex = groupStartIndex;
	}

	public long getMyTotalCount() {
		return myTotalCount;
	}

	public void setMyTotalCount(long myTotalCount) {
		this.myTotalCount = myTotalCount;
	}

	public long getGroupTotalCount() {
		return groupTotalCount;
	}

	public void setGroupTotalCount(long groupTotalCount) {
		this.groupTotalCount = groupTotalCount;
	}

	public Map<String, List<ActivityHistory>> getMyPaginationList() {
		return myPaginationList;
	}

	public void setMyPaginationList(
			Map<String, List<ActivityHistory>> myPaginationList) {
		this.myPaginationList = myPaginationList;
	}

	public Map<String, List<ActivityHistory>> getGroupPaginationList() {
		return groupPaginationList;
	}

	public void setGroupPaginationList(
			Map<String, List<ActivityHistory>> groupPaginationList) {
		this.groupPaginationList = groupPaginationList;
	}

	public long getStartIndex() {
		return myStartIndex;
	}

	public void setStartIndex(int myStartIndex) {
		this.myStartIndex = myStartIndex;
	}

	public int getMaxFetchCount() {
		return maxFetchCount;
	}

	public void setMaxFetchCount(int maxFetchCount) {
		this.maxFetchCount = maxFetchCount;
	}

	public void setAllActivities(List<ActivityHistory> allActivities) {
		this.allActivities = allActivities;
	}

	public List<ActivityHistory> getAllActivities() {
		return allActivities;
	}

	public void setActivityService(IActivityHistoryService activityService) {
		this.activityService = activityService;
	}

	public IActivityHistoryService getActivityService() {
		return activityService;
	}

	public List<ActivityHistory> getMyActivity() {
		return myActivity;
	}

	public void setMyActivity(List<ActivityHistory> myActivity) {
		this.myActivity = myActivity;
	}

	public List<ActivityHistory> getGroupActivity() {
		return groupActivity;
	}

	public void setGroupActivity(List<ActivityHistory> groupActivity) {
		this.groupActivity = groupActivity;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	@PostConstruct
	public void init() {
		log.debug("init of ActivityHistory bean is called and fetching your and group related activities!");

		// loding first ten results of my activity from the activity_history
		myStartIndex = 0;
		maxFetchCount = 10;

		myTotalCount = activityService.getCountOfAllMyActivites(dashBean
				.getCurrentEventSecurityUser().getSecurityUser()
				.getSecurityUserId(), dashBean.getCurrentEventId());

		if (myTotalCount > 0) {
			log.debug("total count of my actviity is:" + myTotalCount);

			myActivity = activityService.getAllMyLatestActivities(myStartIndex,
					maxFetchCount, dashBean.getCurrentEventSecurityUser()
							.getSecurityUser().getSecurityUserId(),
					dashBean.getCurrentEventId());

			if (myActivity != null) {
				log.debug("Total myactivity records found in db for the first time is"
						+ myActivity.size());
			}
		}

		if (myTotalCount > 10) {

			myTotalPages = (int) (myTotalCount / 10);

			if ((myTotalCount % 10) > 0) {
				myTotalPages += 1;
			}

			log.debug("total my activity count is:" + myTotalCount
					+ " is divided by 10 is equal to number of pages");
			log.debug("total my activity pages is:" + myTotalPages);

		} else {
			myTotalPages = 1;

			log.debug("total my activity count is:" + myTotalCount
					+ " is divided by 10 is equal to number of pages");
			log.debug("total my activity pages is:" + myTotalPages);
		}

		myPaginationList = new HashMap<String, List<ActivityHistory>>();
		myPaginationList.put("1", myActivity);
		setMyCurrentPage(1);

		// loding first ten results of group activity from the activity_history
		groupStartIndex = 0;

		groupTotalCount = activityService.getCountOfAllActivitesOfGroup(
				dashBean.getCurrentEventSecurityUser().getSecurityUser()
						.getSecurityUserId(), dashBean.getCurrentEventId());

		if (groupTotalCount > 0) {
			log.debug("total count of activities of the group:"
					+ groupTotalCount);

			groupActivity = activityService.getAllLatestGroupActivities(
					groupStartIndex, maxFetchCount, dashBean
							.getCurrentEventSecurityUser().getSecurityUser()
							.getSecurityUserId(), dashBean.getCurrentEventId());

		}

		if (groupTotalCount > 10) {

			groupTotalPages = (int) (groupTotalCount / 10);

			if ((groupTotalCount % 10) > 0) {
				groupTotalPages += 1;
			}

			log.debug("total group activity count is:" + groupTotalCount
					+ " is divided by 10 is equal to number of pages");
			log.debug("total group activity pages is:" + groupTotalPages);

		} else {
			groupTotalPages = 1;

			log.debug("total group activity count is:" + groupTotalCount
					+ " is divided by 10 is equal to number of pages");
			log.debug("total group activity pages is:" + groupTotalPages);
		}

		groupPaginationList = new HashMap<String, List<ActivityHistory>>();
		groupPaginationList.put("1", groupActivity);
		setGroupCurrentPage(1);
	}

	public String getActivityType(long activityTypeId) {
		if (activityTypeId == ACTIVITY_TYPE.IMAGE_LIKE.id()) {
			component = " image";
			return " likes ";
		} else if (activityTypeId == ACTIVITY_TYPE.IMAGE_TAG.id()) {
			component = " image";
			return " tagged ";
		} else if (activityTypeId == ACTIVITY_TYPE.IMAGE_DELETE.id()) {
			component = null;
			return " has deleted the image!";
		} else if (activityTypeId == ACTIVITY_TYPE.IMAGE_FLAG.id()) {
			component = " image";
			return " flagged ";
		} else if (activityTypeId == ACTIVITY_TYPE.COMMENT_ADD.id()) {
			component = null;
			return " has added the new comment!";
		} else if (activityTypeId == ACTIVITY_TYPE.COMMENT_DELETE.id()) {
			component = null;
			return " has deleted the comment!";
		} else if (activityTypeId == ACTIVITY_TYPE.COMMENT_LIKE.id()) {
			component = " comment";
			return " likes ";
		} else if (activityTypeId == ACTIVITY_TYPE.COMMENT_FLAG.id()) {
			component = " comment";
			return " flagged ";
		} else if (activityTypeId == ACTIVITY_TYPE.IMAGE_ADD.id()) {
			component = null;
			return " has added the new image! ";
		}
		return null;
	}

	public String goToNextForMyActivity() {
		myCurrentPage += 1;
		if (myTotalPages >= myCurrentPage) {
			myStartIndex += 10;
			if (myPaginationList.containsKey(myCurrentPage + "")) {
				log.debug("this page has already bean saved in map!");

				myActivity = myPaginationList.get(myCurrentPage + "");
			} else {

				log.debug("this page is not available, fetching from db!");

				myActivity = activityService.getAllMyLatestActivities(
						myStartIndex, maxFetchCount, dashBean
								.getCurrentEventSecurityUser()
								.getSecurityUser().getSecurityUserId(),
						dashBean.getCurrentEventId());

				myPaginationList.put(myCurrentPage + "", myActivity);
			}
		}
		return null;
	}

	public String goToPreviousForMyActivity() {
		myCurrentPage -= 1;
		if (myTotalPages >= myCurrentPage) {
			myStartIndex -= 10;
			if (myPaginationList.containsKey(myCurrentPage + "")) {
				log.debug("this page has already bean saved in map!");

				myActivity = myPaginationList.get(myCurrentPage + "");
			} else {

				log.debug("this page is not available, fetching from db!");

				myActivity = activityService.getAllMyLatestActivities(
						myStartIndex, maxFetchCount, dashBean
								.getCurrentEventSecurityUser()
								.getSecurityUser().getSecurityUserId(),
						dashBean.getCurrentEventId());

				myPaginationList.put(myCurrentPage + "", myActivity);
			}
		}
		return null;
	}

	public String goToNextForGroupActivity() {
		groupCurrentPage += 1;
		if (groupTotalPages >= groupCurrentPage) {
			groupStartIndex += 10;
			if (groupPaginationList.containsKey(groupCurrentPage + "")) {
				log.debug("this page has already bean saved in map!");

				groupActivity = groupPaginationList.get(groupCurrentPage + "");
			} else {

				log.debug("this page is not available, fetching from db!");

				groupActivity = activityService.getAllLatestGroupActivities(
						groupStartIndex, maxFetchCount, dashBean
								.getCurrentEventSecurityUser()
								.getSecurityUser().getSecurityUserId(),
						dashBean.getCurrentEventId());

				groupPaginationList.put(groupCurrentPage + "", groupActivity);
			}
		}
		return null;
	}

	public String goToPreviousForGroupActivity() {
		groupCurrentPage -= 1;
		if (groupTotalPages >= groupCurrentPage) {
			groupStartIndex -= 10;
			if (groupPaginationList.containsKey(groupCurrentPage + "")) {
				log.debug("this page has already bean saved in map!");

				groupActivity = groupPaginationList.get(groupCurrentPage + "");
			} else {

				log.debug("this page is not available, fetching from db!");

				groupActivity = activityService.getAllMyLatestActivities(
						groupStartIndex, maxFetchCount, dashBean
								.getCurrentEventSecurityUser()
								.getSecurityUser().getSecurityUserId(),
						dashBean.getCurrentEventId());

				groupPaginationList.put(groupCurrentPage + "", groupActivity);
			}
		}
		return null;
	}
}
