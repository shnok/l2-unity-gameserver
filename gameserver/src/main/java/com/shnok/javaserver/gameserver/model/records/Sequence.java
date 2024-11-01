package com.shnok.javaserver.gameserver.model.records;

public record Sequence(int sequenceId, int objectId, int dist, int yaw, int pitch, int time, int duration, int turn, int rise, int widescreen)
{
}