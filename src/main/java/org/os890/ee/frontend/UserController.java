/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.os890.ee.frontend;

import org.apache.deltaspike.core.api.config.view.controller.PreRenderView;
import org.apache.deltaspike.core.api.scope.GroupedConversation;
import org.apache.deltaspike.core.api.scope.GroupedConversationScoped;
import org.apache.deltaspike.jsf.api.message.JsfMessage;
import org.os890.ee.domain.User;
import org.os890.ee.backend.repository.UserRepository;
import org.os890.ee.frontend.config.Pages;
import org.os890.ee.i18n.Messages;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.inject.Inject;

@Named
@GroupedConversationScoped
public class UserController implements Serializable
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private GroupedConversation conversation;

    @Inject
    private JsfMessage<Messages> messages;

    private User currentUser = new User();

    private List<User> userList;

    @PreRenderView
    protected void onPreRenderView()
    {
        this.userList = this.userRepository.findAll();
    }

    public Class<? extends Pages> save()
    {
        this.userRepository.save(this.currentUser);
        this.messages.addInfo().msgSaved(this.currentUser.getNickName());

        this.conversation.close();
        return Pages.UserList.class;
    }

    public Class<? extends Pages> createUser()
    {
        this.conversation.close();
        return Pages.CreateOrEditUser.class;
    }

    public Class<? extends Pages> editUser(User user)
    {
        this.currentUser = user;
        return Pages.CreateOrEditUser.class;
    }

    public void deleteUser(User user)
    {
        this.userRepository.attachAndRemove(user);
        this.messages.addInfo().msgDeleted(user.getNickName());
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public List<User> getUserList()
    {
        return userList;
    }
}
