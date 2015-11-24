${title}<br/>
目标地址:<a href="${urlHelper.getUrlBySpace(space)}/blog/${comment.scope.scopeId}#comment-container">${urlHelper.getUrlBySpace(space)}/blog/${comment.scope.scopeId}#comment-container</a>
<br/>
<#if comment.parent ?exists>
回复内容:
<#if comment.content?length gt 50>
    ${comment.content?substring(0,50)}...
    <#else>
    ${comment.content}
</#if>
<br/>
你的评论:
<#if comment.reply.content?length gt 50>
    ${comment.reply.content?substring(0,50)}...
    <#else>
    ${comment.reply.content}
</#if>
<#else>
内容:<#if comment.content?length gt 50>
    ${comment.content?substring(0,50)}...
    <#else>
    ${comment.content}
</#if>
</#if>