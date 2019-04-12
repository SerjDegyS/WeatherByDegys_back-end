<#import "parts/common.ftl" as c>

<@c.page>
List of users

<table>
    <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <#list userList as user>
        <tr>
            <td>${user.name}</td>
            <td><#list user.roles as role>${role}<#sep> | </#list></td>
            <td><a href="/main/edit/${user.id}">edit</a></td>
        </tr>
    </#list>
    </tbody>
</table>

</@c.page>