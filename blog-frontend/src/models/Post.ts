import type { Comment } from '../models/Comment';
import type { User } from "./User";

export interface Post {

    id: string;
    title: string;
    author: User;
    content: string;
    createdAt: Date;
    comments: Comment[];
}