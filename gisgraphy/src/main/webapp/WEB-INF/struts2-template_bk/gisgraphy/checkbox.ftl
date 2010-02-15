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
		var checked = dojo.byId("${parameters.id?html}").checked;
		<@handleDefaultOverrides overrides="overrides"/>
		new dijit.form.CheckBox(overrides, dojo.byId("${parameters.id?html}"));
		// for some reason, struts2 wont take into consideration the default value of checkboxes...
		dijit.byId("${parameters.id?html}").setAttribute('value', '${parameters.fieldValue?html}');
		dijit.byId("${parameters.id?html}").setValue(checked);
	});
</script>
<#--
NOTE: The 'header' stuff that follows is in this one file for checkbox due to the fact
that for checkboxes we do not want the label field to show up as checkboxes handle their own
lables
-->
<#assign hasFieldErrors = fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<div <#rt/><#if parameters.id?exists>id="wwgrp_${parameters.id}"<#rt/></#if> class="wwgrp">

<#if hasFieldErrors>
<div <#rt/><#if parameters.id?exists>id="wwerr_${parameters.id}"<#rt/></#if> class="wwerr">
<#list fieldErrors[parameters.name] as error>
    <div<#rt/>
    <#if parameters.id?exists>
     errorFor="${parameters.id}"<#rt/>
    </#if>
    class="errorMessage">
             ${error?html}
    </div><#t/>
</#list>
</div><#t/>
</#if>
<#if parameters.labelposition?default("") == 'left'>
<span <#rt/>
<#if parameters.id?exists>id="wwlbl_${parameters.id}"<#rt/></#if> class="wwlbl">
<label<#t/>
<#if parameters.id?exists>
 for="${parameters.id?html}"<#rt/>
</#if>
<#if hasFieldErrors>
 class="checkboxErrorLabel"<#rt/>
<#else>
 class="label"<#rt/>
</#if>
>${parameters.label?html}</label><#rt/>
</span>
</#if>

<#if parameters.labelposition?default("top") == 'top'>
<div <#rt/>
<#else>
<span <#rt/>
</#if>
<#if parameters.id?exists>id="wwctrl_${parameters.id}"<#rt/></#if> class="wwctrl">

<#if parameters.required?default(false)>
        <span class="required">*</span><#t/>
</#if>

<#include "/${parameters.templateDir}/simple/checkbox.ftl" />
<#if parameters.labelposition?default("") != 'left'>
<#if parameters.labelposition?default("top") == 'top'>
</div> <#rt/>
<#else>
</span>  <#rt/>
</#if>
<#if parameters.label?exists>
<#if parameters.labelposition?default("top") == 'top'>
<div <#rt/>
<#else>
<span <#rt/>
</#if>
<#if parameters.id?exists>id="wwlbl_${parameters.id}"<#rt/></#if> class="wwlbl">
<label<#t/>
<#if parameters.id?exists>
 for="${parameters.id?html}"<#rt/>
</#if>
<#if hasFieldErrors>
 class="checkboxErrorLabel"<#rt/>
<#else>
 class="checkboxLabel"<#rt/>
</#if>
>${parameters.label?html}</label><#rt/>
</#if>
</#if>
<#-- replaced by include controlfooter
<#if parameters.label?exists>
<#if parameters.labelposition?default("top") == 'top'>
</div> <#rt/>
<#else>
</span> <#rt/>
</#if>
</#if>
</div>-->
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" /><#nt/>
