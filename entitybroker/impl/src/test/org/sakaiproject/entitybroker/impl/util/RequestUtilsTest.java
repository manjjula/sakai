/**
 * $Id$
 * $URL$
 * RequestUtilsTest.java - entity-broker - Jul 28, 2008 7:55:59 AM - azeckoski
 **************************************************************************
 * Copyright 2008 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.sakaiproject.entitybroker.impl.util;

import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Order;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.mocks.MockEBHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;


/**
 * testing the request utilities
 * 
 * @author Aaron Zeckoski (azeckoski @ gmail.com)
 */
public class RequestUtilsTest extends TestCase {

   /**
    * Test method for {@link org.sakaiproject.entitybroker.impl.util.RequestUtils#makeSearchFromRequest(javax.servlet.http.HttpServletRequest)}.
    */
   public void testMakeSearchFromRequest() {
      Search search = null;
      MockEBHttpServletRequest req = null;

      req = new MockEBHttpServletRequest("GET", new String[] {});
      search = RequestUtils.makeSearchFromRequest(req);
      assertNotNull(search);
      assertTrue( search.isEmpty() );
      assertEquals(0, search.getRestrictions().length);
      search.addOrder( new Order("test") );

      req = new MockEBHttpServletRequest("GET", "test", "stuff");
      search = RequestUtils.makeSearchFromRequest(req);
      assertNotNull(search);
      assertFalse( search.isEmpty() );
      assertEquals(1, search.getRestrictions().length);
      assertNotNull( search.getRestrictionByProperty("test") );
      assertEquals("stuff", search.getRestrictionByProperty("test").value);

      // make sure _method is ignored
      req = new MockEBHttpServletRequest("GET", "test", "stuff", "_method", "PUT");
      search = RequestUtils.makeSearchFromRequest(req);
      assertNotNull(search);
      assertFalse( search.isEmpty() );
      assertEquals(1, search.getRestrictions().length);
      assertNotNull( search.getRestrictionByProperty("test") );
      assertEquals("stuff", search.getRestrictionByProperty("test").value);

      req = new MockEBHttpServletRequest("GET", "test", "stuff", "other", "more");
      search = RequestUtils.makeSearchFromRequest(req);
      assertNotNull(search);
      assertFalse( search.isEmpty() );
      assertEquals(2, search.getRestrictions().length);
      assertNotNull( search.getRestrictionByProperty("test") );
      assertEquals("stuff", search.getRestrictionByProperty("test").value);
      assertNotNull( search.getRestrictionByProperty("other") );
      assertEquals("more", search.getRestrictionByProperty("other").value);
   }

   /**
    * Test method for {@link org.sakaiproject.entitybroker.impl.util.RequestUtils#setResponseEncoding(java.lang.String, javax.servlet.http.HttpServletResponse)}.
    */
   public void testSetResponseEncoding() {
      MockHttpServletResponse res = new MockHttpServletResponse();

      RequestUtils.setResponseEncoding(Formats.XML, res);
      assertEquals(Formats.UTF_8, res.getCharacterEncoding());
      assertEquals(Formats.XML_MIME_TYPE, res.getContentType());
   }

}
