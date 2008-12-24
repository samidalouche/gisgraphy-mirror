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
<#--
Complete abuse of Struts2 Theme system to add support for Dojo's Slider.. 
The only link between hidden and slider is that the slider uses hidden field to pass 
the parameters inside the form.

Non standard Parameters :
- dojoType: "HorizontalSlider"
- zeroPercentLabelKey : displayed at 0%
- fiftyPercentLabelKey : displayed at  50%
- parameters.hundredPercentLabelKey : displayed at 100%
-->
	
<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />
<#include "/${parameters.templateDir}/simple/hidden.ftl" />
<#if parameters.dojoType?exists>
	<#if parameters.dojoType=="HorizontalSlider">
		<script type="text/javascript">
			dojo.require("dijit.form.Slider");
		
			dojo.addOnLoad(function() {
				var value = dojo.byId("${parameters.id?html}").value;
				
				var overrides = {};
				overrides.minimum = 0;
				overrides.maximum = 10;
				overrides.showButtons = false;
				overrides.intermediateChanges = true;
				overrides.discreteValues = 11;
				
				overrides.onChange = function(value) {
						dojo.byId('${parameters.id?html}').value=value;
				};
				
				var slider = new dijit.form.HorizontalSlider(overrides, 
					dojo.byId("${parameters.id?html}_horizontalSlider"));
				
				var topRuleLabels = new dijit.form.HorizontalRuleLabels( {
					container: "topDecoration"
				}, dojo.byId("${parameters.id?html}_horizontalSlider_topHorizontalRuleLabels"));
				
				var topHorizontalRule = new dijit.form.HorizontalRule( {
					container: "topDecoration",
					count: 11
				}, dojo.byId("${parameters.id?html}_horizontalSlider_topHorizontalRule"));
				
				var bottomHorizontalRule = new dijit.form.HorizontalRule( {
					container: "bottomDecoration",
					count: 5
				}, dojo.byId("${parameters.id?html}_horizontalSlider_bottomHorizontalRule"));
				
				var bottomRuleLabels = new dijit.form.HorizontalRuleLabels( {
					container: "bottomDecoration"
				}, dojo.byId("${parameters.id?html}_horizontalSlider_bottomHorizontalRuleLabels"));
				
				slider.addChild(topRuleLabels);
				slider.addChild(topHorizontalRule);
				slider.addChild(bottomHorizontalRule);
				slider.addChild(bottomRuleLabels);
				slider.startup();
				dijit.byId("${parameters.id?html}_horizontalSlider").setValue(value);
			});
		</script>

		<div id="${parameters.id?html}_horizontalSlider" class="dojo-horizontalSlider">
			<ol id="${parameters.id?html}_horizontalSlider_topHorizontalRuleLabels" class="dojo-topHorizontalRuleLabels">
				<li>0%</li>
				<li>20%</li>
				<li>40%</li>
				<li>60%</li>
				<li>80%</li>
				<li>100%</li>
			</ol>
			<div id="${parameters.id?html}_horizontalSlider_topHorizontalRule" class="dojo-topHorizontalRule"></div>
			<div id="${parameters.id?html}_horizontalSlider_bottomHorizontalRule" class="dojo-bottomHorizontalRule"></div>
	
			<ol id="${parameters.id?html}_horizontalSlider_bottomHorizontalRuleLabels" class="dojo-bottomHorizontalRuleLabels">
				<li><#if parameters.zeroPercentLabelKey?exists><@s.text name="${parameters.zeroPercentLabelKey}" /></#if></li>
				<li><#if parameters.fiftyPercentLabelKey?exists><@s.text name="${parameters.fiftyPercentLabelKey}" /></#if></li>
				<li><#if parameters.hundredPercentLabelKey?exists><@s.text name="${parameters.hundredPercentLabelKey}" /></#if></li>
			</ol>
		</div>
	</#if>
</#if>
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />