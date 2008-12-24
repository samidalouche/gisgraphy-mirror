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
	dojo.require("dijit.form.Textarea");
	dojo.addOnLoad(
		function() {
			var value = dojo.byId("${parameters.id?html}").value;
			<@handleDefaultOverrides overrides="overrides"/>
			var textarea = new dijit.form.Textarea( overrides , dojo.byId("${parameters.id?html}"));
			dijit.byId("${parameters.id?html}").setValue(value);
		});
</script>
<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />
<#include "/${parameters.templateDir}/simple/textarea.ftl" />
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />
