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
<#-- Non-standard parameters :
 - dojoType: "CurrencyTextBox", "NumberTextBox", "ValidationTextBox", "NumberSpinner"
 - regExp
 - dojoSmallDelta : delta of the number spinner
 - dojoLargeDelta : delta of the number spinner
 - dojoConstraints : maps to ValidationTextBox's constraints.
-->
<script type="text/javascript">
	<#if parameters.dojoType?exists>
		<#if parameters.dojoType=="CurrencyTextBox">
			dojo.require("dijit.form.CurrencyTextBox");
			dojo.addOnLoad(function() {
				var value = dojo.byId("${parameters.id?html}").value;
				<@handleDefaultOverrides overrides="overrides"/>
				<#if parameters.dojoCurrency?exists>
						overrides.currency = '${parameters.dojoCurrency}';
				</#if>
				new dijit.form.CurrencyTextBox(overrides, dojo.byId("${parameters.id?html}"));
				dijit.byId("${parameters.id?html}").setValue(value);
			});
		<#elseif parameters.dojoType=="DateTimePicker">
			<#include "/${parameters.templateDir}/${parameters.theme}/common-dojo.ftl" />
			<#-- Integration is far from being complete..-->
//<![CDATA[
				dojo.require("dijit.form.DateTextBox");
				dojo.require("dojo.date.stamp");
				dojo.addOnLoad(function() {
					var value = dojo.byId("${parameters.id?html}").value;
					<@handleDefaultOverrides overrides="overrides"/>
					<#--
					${localizationHelper.getBestDateTimePattern()}
					dd/MM/yyyy
					overrides.constraints.datePattern = '${localizationHelper.getBestDateTimePattern()}';
					-->
					
					new dijit.form.DateTextBox(overrides, dojo.byId("${parameters.id?html}"));
					if(value != null && value.length > 0) {
						var dateValue = dojo.date.stamp.fromISOString(value);
						dijit.byId("${parameters.id?html}").setValue(dateValue);
					}
				});
//]]>
			</script>
		<#elseif (parameters.dojoType=="NumberTextBox")>
			dojo.require("dijit.form.NumberTextBox");
			dojo.addOnLoad(function() {
				var value = dojo.byId("${parameters.id?html}").value;
				<@handleDefaultOverrides overrides="overrides"/>
				new dijit.form.NumberTextBox(overrides, dojo.byId("${parameters.id?html}"));
				dijit.byId("${parameters.id?html}").setValue(value);
			});
		<#elseif parameters.dojoType=="ValidationTextBox">
			dojo.require("dijit.form.ValidationTextBox");
			dojo.addOnLoad(function() {
				var value = dojo.byId("${parameters.id?html}").value;
				
				<@handleDefaultOverrides overrides="overrides"/>
				overrides.regExp = '${parameters.regExp?default(".*")}'; 
				
				new dijit.form.ValidationTextBox( overrides, dojo.byId("${parameters.id?html}"));
				dijit.byId("${parameters.id?html}").setValue(value);
			});
		<#elseif parameters.dojoType=="NumberSpinner">
			dojo.require("dijit.form.NumberSpinner");
			dojo.addOnLoad(function() {
				var value = dojo.byId("${parameters.id?html}").value;
				
				<@handleDefaultOverrides overrides="overrides"/>
				overrides.smallDelta =  ${parameters.dojoSmallDelta?default(1)};
				overrides.largeDelta = ${parameters.dojoLargeDelta?default(1)};
				
				new dijit.form.NumberSpinner(overrides, dojo.byId("${parameters.id?html}"));
				dijit.byId("${parameters.id?html}").setValue(value);
			});
		<#elseif parameters.dojoType=="HorizontalSlider">
			<#-- Horizontal Value -->
			<#assign nullValue = -1 />
			<#assign minimumSliderValue=0 />
			<#assign maximumSliderValue=10 />
			<#if parameters.required?default(false)>
				<#assign mustAllowNull = false />
			<#else>
				<#assign mustAllowNull = true />
			</#if>
			<#assign numberOfValues = mustAllowNull?string("12", "11") />
			
			
			dojo.require("dijit.form.Slider");
		
			dojo.addOnLoad(function() {
				// display the only-JS part of the DOM (stuff for dojo)
				dojo.byId('${parameters.id?html}_horizontalSlider').style.display = 'block';
				
				// and add the additional classes to enhance display
				dojo.byId('${parameters.id?html}_inputWrapper').className+=' dojo-slider-input-wrapper-js';
				dojo.byId('${parameters.id?html}').className += ' dojo-slider-input-js';
				
				// disable text input if JS is enabled (because dojo handles the input with the slider)
				dojo.byId('${parameters.id?html}').editable=false;

				var value = dojo.byId("${parameters.id?html}").value;
				// otherwise, it doesn't set the value correctly 
				if(value == null || value == '') {
					value = ${nullValue}
				}
				
				var overrides = {};
				<#if mustAllowNull>
					overrides.minimum = ${nullValue};
				<#else>
					overrides.minimum = ${minimumSliderValue?c};
				</#if>
				overrides.maximum = ${maximumSliderValue?c};
				overrides.showButtons = false;
				overrides.intermediateChanges = true;
				overrides.discreteValues = ${numberOfValues};
				
				overrides.onChange = function(value) {
					if(value == ${nullValue}) {
						dojo.byId('${parameters.id?html}').value='';
					} else {
						dojo.byId('${parameters.id?html}').value=value;
					}
				};
				overrides.class = 'dojo-horizontalSlider';
				
				var slider = new dijit.form.HorizontalSlider(overrides, 
					dojo.byId("${parameters.id?html}_horizontalSlider"));
				
				var topRuleLabels = new dijit.form.HorizontalRuleLabels( {
					container: 'topDecoration',
					class: 'dojo-topHorizontalRuleLabels'
				}, dojo.byId("${parameters.id?html}_horizontalSlider_topHorizontalRuleLabels"));
				
				var topHorizontalRule = new dijit.form.HorizontalRule( {
					container: "topDecoration",
					count: ${numberOfValues},
					class: 'dojo-topHorizontalRule'
				}, dojo.byId("${parameters.id?html}_horizontalSlider_topHorizontalRule"));
				
				var bottomHorizontalRule = new dijit.form.HorizontalRule( {
					container: "bottomDecoration",
					count: ${numberOfValues},
					class: 'dojo-bottomHorizontalRule'
				}, dojo.byId("${parameters.id?html}_horizontalSlider_bottomHorizontalRule"));
				
				var bottomRuleLabels = new dijit.form.HorizontalRuleLabels( {
					container: "bottomDecoration",
					class: 'dojo-bottomHorizontalRuleLabels'
				}, dojo.byId("${parameters.id?html}_horizontalSlider_bottomHorizontalRuleLabels"));
				
				slider.addChild(topRuleLabels);
				slider.addChild(topHorizontalRule);
				slider.addChild(bottomHorizontalRule);
				slider.addChild(bottomRuleLabels);
				slider.startup();
				dijit.byId("${parameters.id?html}_horizontalSlider").setValue(value);
			});	
		</#if>
	</#if>
</script>
<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />

<#if parameters.dojoType=="HorizontalSlider">
	<div id="${parameters.id?html}_horizontalSlider" style="display: none">
		<ol id="${parameters.id?html}_horizontalSlider_topHorizontalRuleLabels">
			<#if mustAllowNull>
				<li><strong class="dojo-slider-notAvailable"><@s.text name="component.tag.text.notAvailable" /></strong></li>
			</#if>
			<li>0</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>5</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>10</li>
		</ol>
		<div id="${parameters.id?html}_horizontalSlider_topHorizontalRule"></div>
		<div id="${parameters.id?html}_horizontalSlider_bottomHorizontalRule"></div>

		<ol id="${parameters.id?html}_horizontalSlider_bottomHorizontalRuleLabels">
			<#if mustAllowNull>
				<li>&nbsp;</li>
			</#if>
			<li><#if parameters.zeroPercentLabelKey?exists><@s.text name="${parameters.zeroPercentLabelKey}" /></#if></li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li><#if parameters.fiftyPercentLabelKey?exists><@s.text name="${parameters.fiftyPercentLabelKey}" /></#if></li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li>&nbsp;</li>
			<li><#if parameters.hundredPercentLabelKey?exists><@s.text name="${parameters.hundredPercentLabelKey}" /></#if></li>
		</ol>
	</div>
</#if>

<#if parameters.dojoType=="HorizontalSlider">
<div id="${parameters.id?html}_inputWrapper" class="dojo-slider-input-wrapper"> 
</#if>
<#include "/${parameters.templateDir}/simple/text.ftl" /> 

<#if parameters.dojoType=="HorizontalSlider">
<noscript>
	<@s.text name="component.tag.text.between">
		<@s.param><strong>${minimumSliderValue?c}</strong></@s.param>
		<@s.param><strong><@s.text name="component.tag.text.easy" /></strong></@s.param>
		<@s.param><strong>${maximumSliderValue?c}</strong></@s.param>
		<@s.param><strong><@s.text name="component.tag.text.impossible" /></strong></@s.param>
	</@s.text>
</noscript>
</div>
</#if>

<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />