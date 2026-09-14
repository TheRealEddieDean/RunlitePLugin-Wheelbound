package com.wheelbound;

enum CaTier
{
    EASY("Easy", 3981), MEDIUM("Medium", 3982), HARD("Hard", 3983),
    ELITE("Elite", 3984), MASTER("Master", 3985), GRANDMASTER("Grandmaster", 3986);

    final String title;
    final int enumId;
    CaTier(String title, int enumId) { this.title = title; this.enumId = enumId; }
}
