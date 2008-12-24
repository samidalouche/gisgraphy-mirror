/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.webapp.filter;

import java.util.Locale;

import javax.servlet.jsp.jstl.core.Config;

import junit.framework.TestCase;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.gisgraphy.Constants;

public class LocaleFilterTest extends TestCase {
    private LocaleFilter filter = null;

    @Override
    protected void setUp() throws Exception {
	filter = new LocaleFilter();
	filter.init(new MockFilterConfig());
    }

    public void testSetLocaleInSessionWhenSessionIsNull() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("locale", "es");

	MockHttpServletResponse response = new MockHttpServletResponse();
	filter.doFilter(request, response, new MockFilterChain());

	// no session, should result in null
	assertNull(request.getSession().getAttribute(
		Constants.PREFERRED_LOCALE_KEY));
	// thread locale should always have it, regardless of session
	assertNotNull(LocaleContextHolder.getLocale());
    }

    public void testSetLocaleInSessionWhenSessionNotNull() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("locale", "es");

	MockHttpServletResponse response = new MockHttpServletResponse();
	request.setSession(new MockHttpSession(null));

	filter.doFilter(request, response, new MockFilterChain());

	// session not null, should result in not null
	Locale locale = (Locale) request.getSession().getAttribute(
		Constants.PREFERRED_LOCALE_KEY);
	assertNotNull(locale);
	assertNotNull(LocaleContextHolder.getLocale());
	assertEquals(new Locale("es"), locale);
    }

    public void testSetInvalidLocale() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("locale", "foo");

	MockHttpServletResponse response = new MockHttpServletResponse();
	request.setSession(new MockHttpSession(null));

	filter.doFilter(request, response, new MockFilterChain());

	// a locale will get set regardless - there's no such thing as an
	// invalid one
	assertNotNull(request.getSession().getAttribute(
		Constants.PREFERRED_LOCALE_KEY));
    }

    public void testJstlLocaleIsSet() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.addParameter("locale", "es");

	MockHttpServletResponse response = new MockHttpServletResponse();
	request.setSession(new MockHttpSession(null));

	filter.doFilter(request, response, new MockFilterChain());

	assertNotNull(Config.get(request.getSession(), Config.FMT_LOCALE));
    }

    public void testLocaleAndCountry() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest();
	request.setSession(new MockHttpSession());
	request.addParameter("locale", "zh_TW");

	MockHttpServletResponse response = new MockHttpServletResponse();
	filter.doFilter(request, response, new MockFilterChain());

	// session not null, should result in not null
	Locale locale = (Locale) request.getSession().getAttribute(
		Constants.PREFERRED_LOCALE_KEY);
	assertNotNull(locale);
	assertEquals(new Locale("zh", "TW"), locale);
    }
}
