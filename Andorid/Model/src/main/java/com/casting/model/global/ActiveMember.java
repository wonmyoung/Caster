package com.casting.model.global;

import com.casting.model.Member;

import java.util.Observable;

public class ActiveMember extends Observable {

    private static class LazyHolder
    {
        private static ActiveMember mInstance = new ActiveMember();
    }

    public static ActiveMember getInstance()
    {
        return LazyHolder.mInstance;
    }

    private Member  mMember;

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member member) {

        this.mMember = member;

        setChanged();

        notifyObservers(member);
    }

    public String getEmailAddress()
    {
        return (mMember == null ? null : mMember.getEmail());
    }

    public int getUserCap()
    {
        return (mMember == null ? 0 : mMember.getUserPoint());
    }
}
