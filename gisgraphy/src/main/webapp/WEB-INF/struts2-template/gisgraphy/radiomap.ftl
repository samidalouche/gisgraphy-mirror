<#--
/*
 * $Id: Action.java 502296 2007-02-01 17:33:39Z niallp $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
-->
<#include "/${parameters.templateDir}/${parameters.theme}/common-dojo.ftl" />
<script type="text/javascript">
	dojo.require("dijit.form.CheckBox");
	
	dojo.addOnLoad(function() {
		<@s.iterator value="parameters.list">
			<#-- Copy paste from radiomap.ftl -->
			<#if parameters.listKey?exists>
		        <#assign itemKey = stack.findValue(parameters.listKey)/>
		    <#else>
		        <#assign itemKey = stack.findValue('top')/>
		    </#if>
		    <#assign itemKeyStr = itemKey.toString() />
		    
		    <#-- we can now build the IDs of each of the elements -->
		    var checked = dojo.byId("${parameters.id?html}${itemKeyStr?html}").checked;
		    var value = dojo.byId("${parameters.id?html}${itemKeyStr?html}").value;
		    new dijit.form.RadioButton({name: dojo.byId("${parameters.id?html}${itemKeyStr?html}").name}, dojo.byId("${parameters.id?html}${itemKeyStr?html}"));
			dijit.byId("${parameters.id?html}${itemKeyStr?html}").setAttribute('value', value);
			dijit.byId("${parameters.id?html}${itemKeyStr?html}").setValue(checked);
		    //dijit.byId("${parameters.id?html}${itemKeyStr?html}").setChecked(checked);
		    // for some reason, struts2 wont take into consideration the default value
			// of radioboxes...
			//dijit.byId("${parameters.id?html}${itemKeyStr?html}").setValue(value);
		</@s.iterator>
	});
</script>
<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />
<#include "/${parameters.templateDir}/simple/radiomap.ftl" />
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />
<#nt/>
