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
${parameters.after?if_exists}<#t/>
    <#lt/>
<#if parameters.labelposition?default("top") == 'top'>
</div> <#rt/>
<#else>
</span> <#rt/>
</#if>
<#if parameters.wwinfo?exists><div class="${parameters.wwinfoCssClass?default("wwinfo")}"><label for="${parameters.id}">${parameters.wwinfo}</label></div></#if>
<#if parameters.wwdescription?exists>
<div class="wwdescription">
	<label for="${parameters.id}">${parameters.wwdescription}</label>
</div>
</#if>
<#-- Add the stupid clear hack because of the float'ed divs -->
<div class="clearhack"></div>
</div>