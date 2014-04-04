package com.coombu.photobook.model;




/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

/**
* Models core user information retrieved by a {@link UserDetailsService}.
* <p>
* Developers may use this class directly, subclass it, or write their own {@link UserDetails} implementation from
* scratch.
* <p>
* {@code equals} and {@code hashcode} implementations are based on the {@code username} property only, as the
* intention is that lookups of the same user principal object (in a user registry, for example) will match
* where the objects represent the same user, not just when all the properties (authorities, password for
* example) are the same.
* <p>
* Note that this implementation is not immutable. It implements the {@code CredentialsContainer} interface, in order
* to allow the password to be erased after authentication. This may cause side-effects if you are storing instances
* in-memory and reusing them. If so, make sure you return a copy from your {@code UserDetailsService} each time it is
* invoked.
*
* @author Ben Alex
* @author Luke Taylor
* @author Fekade - extension
*/
public class User implements UserDetails, CredentialsContainer, Serializable {

   private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

   //~ Instance fields ================================================================================================
   private String password;
   private final String username;
   private final Set<GrantedAuthority> authorities;
   private final boolean accountNonExpired;
   private final boolean accountNonLocked;
   private final boolean credentialsNonExpired;
   private final boolean enabled;
   private long userStatusTypeId;
   private String firstName;
   private String lastName;
   private Date lastLoggedInTimestamp;
   private long securityUserId;
   private LoginHistory loginHistory;
   //~ Constructors ===================================================================================================



   public User(String username, String password, boolean enabled, boolean accountNonExpired,
           boolean credentialsNonExpired, boolean accountNonLocked, long securityUserId, 
           String firstName, String lastName, long userStatusTypeId, Date lastLoggedInTimestamp,
           Collection<? extends GrantedAuthority> authorities) {	   
	   this(username, password, enabled, accountNonExpired,  credentialsNonExpired, accountNonLocked, authorities);
	   this.setSecurityUserId(securityUserId);
	   this.firstName = firstName;
	   this.lastName = lastName;
	   this.userStatusTypeId = userStatusTypeId;
	   this.lastLoggedInTimestamp = lastLoggedInTimestamp;	 	  
	   
   }
   
   /**
    * Calls the more complex constructor with all boolean arguments set to {@code true}.
    */
   public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
       this(username, password, true, true, true, true, authorities);
   }

   /**
    * Construct the <code>User</code> with the details required by
    * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
    *
    * @param username the username presented to the
    *        <code>DaoAuthenticationProvider</code>
    * @param password the password that should be presented to the
    *        <code>DaoAuthenticationProvider</code>
    * @param enabled set to <code>true</code> if the user is enabled
    * @param accountNonExpired set to <code>true</code> if the account has not
    *        expired
    * @param credentialsNonExpired set to <code>true</code> if the credentials
    *        have not expired
    * @param accountNonLocked set to <code>true</code> if the account is not
    *        locked
    * @param authorities the authorities that should be granted to the caller
    *        if they presented the correct username and password and the user
    *        is enabled. Not null.
    *
    * @throws IllegalArgumentException if a <code>null</code> value was passed
    *         either as a parameter or as an element in the
    *         <code>GrantedAuthority</code> collection
    */
   public User(String username, String password, boolean enabled, boolean accountNonExpired,
           boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

       if (((username == null) || "".equals(username)) || (password == null)) {
           throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
       }

       this.username = username;
       this.password = password;
       this.enabled = enabled;
       this.accountNonExpired = accountNonExpired;
       this.credentialsNonExpired = credentialsNonExpired;
       this.accountNonLocked = accountNonLocked;
       this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
   }

   //~ Methods ========================================================================================================

   public Collection<GrantedAuthority> getAuthorities() {
       return authorities;
   }

   public String getPassword() {
       return password;
   }

   public String getUsername() {
       return username;
   }

/**
 * @return the userStatusTypeId
 */
public long getUserStatusTypeId() {
	return userStatusTypeId;
}

/**
 * @return the firstName
 */
public String getFirstName() {
	return firstName;
}

/**
 * @return the lastName
 */
public String getLastName() {
	return lastName;
}

/**
 * @return the lastLoggedInTimestamp
 */
public Date getLastLoggedInTimestamp() {
	return lastLoggedInTimestamp;
}

/**
 * @param loginHistory the loginHistory to set
 */
public void setLoginHistory(LoginHistory loginHistory) {
	this.loginHistory = loginHistory;
}

/**
 * @return the loginHistory
 */
public LoginHistory getLoginHistory() {
	return loginHistory;
}

public boolean isEnabled() {
       return enabled;
   }

   public boolean isAccountNonExpired() {
       return accountNonExpired;
   }

   public boolean isAccountNonLocked() {
       return accountNonLocked;
   }

   public boolean isCredentialsNonExpired() {
       return credentialsNonExpired;
   }

   public void eraseCredentials() {
       password = null;
   }

   private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
       Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
       // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
       SortedSet<GrantedAuthority> sortedAuthorities =
           new TreeSet<GrantedAuthority>(new AuthorityComparator());

       for (GrantedAuthority grantedAuthority : authorities) {
           Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
           sortedAuthorities.add(grantedAuthority);
       }

       return sortedAuthorities;
   }

   private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
       private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

       public int compare(GrantedAuthority g1, GrantedAuthority g2) {
           // Neither should ever be null as each entry is checked before adding it to the set.
           // If the authority is null, it is a custom authority and should precede others.
           if (g2.getAuthority() == null) {
               return -1;
           }

           if (g1.getAuthority() == null) {
               return 1;
           }

           return g1.getAuthority().compareTo(g2.getAuthority());
       }
   }

   /**
    * Returns {@code true} if the supplied object is a {@code User} instance with the
    * same {@code username} value.
    * <p>
    * In other words, the objects are equal if they have the same username, representing the
    * same principal.
    */
   @Override
   public boolean equals(Object rhs) {
       if (rhs instanceof User) {
           return username.equals(((User) rhs).username);
       }
       return false;
   }

   /**
    * Returns the hashcode of the {@code username}.
    */
   @Override
   public int hashCode() {
       return username.hashCode();
   }

   @Override
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append(super.toString()).append(": ");
       sb.append("Username: ").append(this.username).append("; ");
       sb.append("Password: [PROTECTED]; ");
       sb.append("Enabled: ").append(this.enabled).append("; ");
       sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
       sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
       sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");
       sb.append("firstName: ").append(this.firstName).append("; ");
       sb.append("lastName: ").append(this.lastName).append("; ");
       sb.append("userStatusTypeId: ").append(this.userStatusTypeId).append("; ");
       sb.append("lastLoggedInTimestamp: ").append(this.lastLoggedInTimestamp).append("; ");
       if (!authorities.isEmpty()) {
           sb.append("Granted Authorities: ");

           boolean first = true;
           for (GrantedAuthority auth : authorities) {
               if (!first) {
                   sb.append(",");
               }
               first = false;

               sb.append(auth);
           }
       } else {
           sb.append("Not granted any authorities");
       }

       return sb.toString();
   }

   public long getSecurityUserId() 
   {
	   return securityUserId;
   }
   public void setSecurityUserId(long securityUserId)
   {
	   this.securityUserId = securityUserId;
   }
}