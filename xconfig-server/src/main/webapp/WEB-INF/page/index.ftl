<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["plugin/typeahaed.bundle.0.11.1.js"] remoteJsFiles=[] localCssFiles=[]>
    <div class="input-group input-group-lg">
        <input id="search" type="text" class="typeahead" placeholder="Search for...">
          <span class="input-group-btn">
            <button class="btn btn-default" type="button">Go!</button>
          </span>
    </div>
    <hr/>

    <div style="font-size: x-large">
        <#list projects as project>
            <#--todo 为这些标签增加不同的颜色 -->
            <a class="label label-info" href="${basepath}/main/project?project=${project?html}">${project?html}</a>
        </#list>
    </div>

<input class="typeahead" type="text" placeholder="States of USA">

    <script type="text/javascript">
        var provinces = ["广东省", "福建省", "山西省", "山东省", "湖北省", "湖南省", "陕西省", "上海市", "北京市", "广西省"];
        var substringMatcher = function (strs) {
            return function findMatches(q, cb) {
                var matches, substrRegex;
                matches = [];
                substrRegex = new RegExp(q, 'i');
                $.each(strs, function (i, str) {
                    if (substrRegex.test(str)) {
                        matches.push({ value: str });
                    }
                });
                cb(matches);
            };
        };
        $('.typeahead').typeahead({
                    hint: true,
                    highlight: true,
                    minLength: 1
                },
                {
                    name: 'provinces',
                    displayKey: 'value',
                    source: substringMatcher(provinces)
                });

    </script>
</@baseHtml>