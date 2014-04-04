package com.coombu.photobook.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

@Named
@Scope("application")
public class Constants {

	public static final int DEFAULT_BUFFER_SIZE = 8192; // 8KB.

	public static final int MAX_FILE_SIZE = 8388608;

	public static final int MAX_FILE_UPLOAD_LIMIT = 5;

	public static final int WEB_THUMBNAIL_WIDTH = 279;

	public static final int WEB_THUMBNAIL_HEIGHT = 202;

	public static final int WEB_LIGHTBOX_WIDTH = 800;

	public static final int WEB_LIGHTBOX_HEIGHT = 600;

	public static final int PASSWORD_EXPIRE_TIME = 24; //time in hours forgot password token expires
	
	public static final int DAY_IN_MILLI = 24 * 60 * 60 * 1000;

	public static enum SUBMENU_TYPE {
		LATEST(1), 
		MOST_LIKED(2), 
		MOST_COMMENTED(3), 
		MOST_VIEWED(4), 
		MY_UPLOADED(5), 
		TAGGED(6), 
		MY_COMMENTS(7),
		MY_LIKES(8);
		private int id;
		private static final Map<Integer, SUBMENU_TYPE> lookup = new HashMap<Integer, SUBMENU_TYPE>();

		private SUBMENU_TYPE(int pId) {
			this.id = pId;
		}

		static {
			for (final SUBMENU_TYPE s : SUBMENU_TYPE.values()) {
				lookup.put(s.id(), s);
			}
		}

		public int id() {
			return id;
		}

		public static SUBMENU_TYPE getSubMenuType(int i) {
			return lookup.get(i);
		}
	}

	public static enum IMAGE_SOURCE {
		WEB((short) 1),
		MOBILE((short) 2);
		private short id;

		private IMAGE_SOURCE(short pId) {
			this.id = pId;
		}

		public short id() {
			return id;
		}
	}
	
	public static enum IMAGE_STATUS_TYPE {
		PENDING((short) 1), 
		APPROVED((short) 2), 
		DELETED((short) 3),
		ALL((short) 0), 
		FLAGED((short) 4);
		private short id;

		private IMAGE_STATUS_TYPE(short pId) {
			this.id = pId;
		}

		public short id() {
			return id;
		}
	};

	public static enum COMMENT_STATUS_TYPE {
		ACTIVE((short) 1), 
		INACTIVE((short) 2), 
		FLAGED((short) 3), 
		DELETED((short) 4), 
		IMAGE_FLAGGED((short) 5),
		IMAGE_DELETED((short) 6);
		private short id;

		private COMMENT_STATUS_TYPE(short pId) {
			this.id = pId;
		}

		public short id() {
			return id;
		}
	};
	
	public static enum TAG_STATUS_TYPE {
		ACTIVE((short) 1), 
		INACTIVE((short) 2), 
		FLAGED((short) 3);
		private short id;

		private TAG_STATUS_TYPE(short pId) {
			this.id = pId;
		}

		public short id() {
			return id;
		}
	};

	public static enum USER_STATUS_TYPE {
		ACTIVE(1), 
		INACTIVE(2), 
		DELETED(3), 
		PENDING(4), 
		BLOCKED(5);
		private int id;

		private USER_STATUS_TYPE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	};

	public static enum EVENT_STATUS_TYPE {
		ACTIVE(1), 
		INACTIVE(2),
		DELETED(3), 
		PENDING(4), 
		BLOCKED(5);
		private int id;

		private EVENT_STATUS_TYPE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	};

	
	public static enum IMAGE_BUCKET_STATUS_TYPE {
		ACTIVE(1), 
		INACTIVE(2), 
		PARTIALSAVE(3), 
		SAVE(4), 
		PUBLISH(5);
		private int id;

		private IMAGE_BUCKET_STATUS_TYPE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	};
	
	public static enum RESOLUTION_STATUS_TYPE {
		PENDING((short) 1), 
		RESOLVED((short) 2);
		private short id;

		private RESOLUTION_STATUS_TYPE(short pId) {
			this.id = pId;
		}

		public short id() {
			return id;
		}
	};

	public static enum REQUEST_REMOVAL_TYPE {
		COMMENT(1), 
		IMAGE(2), ;
		private int id;

		private REQUEST_REMOVAL_TYPE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	}

	public static enum ROLE {
		ROLE_STUDENT(1), 
		ROLE_REP(2), 
		ROLE_ADMIN(3);
		private int id;

		private ROLE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	};

	public static enum ACTIVITY_TYPE {
		IMAGE_LIKE(1), 
		IMAGE_TAG(2), 
		IMAGE_DELETE(3), 
		IMAGE_FLAG(4), 
		COMMENT_ADD(5), 
		COMMENT_DELETE(6), 
		COMMENT_LIKE(7), 
		COMMENT_FLAG(8), 
		IMAGE_ADD(9);
		private int id;

		private ACTIVITY_TYPE(int pId) {
			this.id = pId;
		}

		public int id() {
			return id;
		}
	};

	public static enum REF_DATA_LIST {
		addressTypelist, bookStsatusTypeList, commentStatusTypeList, countryList, eventTypeTableList, fileTypeList, imageSourceList, imageStatusTypeList, phoneTypeList, privilegeList, requestReasonList, requestRemovalTypeList, ResolutionStatusList, roleList, userStatusList, voteTypeList
	}

	public static int getDefaultBufferSize() {
		return DEFAULT_BUFFER_SIZE;
	}

	public static int getMaxFileSize() {
		return MAX_FILE_SIZE;
	}

	public static int getMaxFileUploadLimit() {
		return MAX_FILE_UPLOAD_LIMIT;
	}
}
